package jp.co.bizreach.robot

import java.io.InputStream
import java.util.Locale

import org.apache.commons.io.input.BOMInputStream

import scala.annotation.tailrec
import scala.io.Source
import scala.util.matching.Regex

import com.softwaremill.quicklens._
import scalaz._
import Scalaz._

/**
 * The object of parsing the robots.txt.
 */
object RobotsTxtParser {
  /**
   * Parse the specified robots.txt.
   *
   * @param stream input stream of robots.txt
   * @param charsetName encoding (optional: defaults to UTF-8)
   * @return the contents of the parsed robots.txt
   */
  def parse(stream: InputStream, charsetName: String = "UTF-8"): RobotsTxt = {
    // Function to analyze by reads each line
    @tailrec
    def readLine0(lines: Iterator[String], robots: RobotsTxt = RobotsTxt(),
                  currentPath: Seq[String] = Nil, isGroupRecord: Boolean = false): RobotsTxt = {
      import Record._

      if(!lines.hasNext) robots
      else read(lines.next()) match {
        // applicable
        case Some((r, v)) => r match {
          case UserAgent =>
            val path = v.toLowerCase(Locale.ENGLISH)
            readLine0(
              lines       = lines,
              robots      = robots += path,
              currentPath = path +: ( if(isGroupRecord) Nil else currentPath )
            )
          case Disallow  =>
            readLine0(
              lines         = lines,
              robots        = robots.update(currentPath: _*)(_.modify(_.disallow).using(v +: _)),
              currentPath   = currentPath,
              isGroupRecord = true
            )
          case Allow =>
            readLine0(
              lines         = lines,
              robots        = robots.update(currentPath: _*)(_.modify(_.allow).using(v +: _)),
              currentPath   = currentPath,
              isGroupRecord = true
            )
          case CrawlDelay =>
            val delay = Seq(v.parseInt.getOrElse(0), 0).max
            readLine0(
              lines         = lines,
              robots        = robots.update(currentPath: _*)(_.modify(_.crawlDelay).setTo(delay)),
              currentPath   = currentPath,
              isGroupRecord = true
            )
          case Sitemap =>
            readLine0(
              lines         = lines,
              robots        = robots.modify(_.sitemap).using(v +: _),
              currentPath   = currentPath,
              isGroupRecord = true
            )
        }
        // not applicable
        case None => readLine0(lines, robots, currentPath, true)
      }
    }

    readLine0(
      Source.fromInputStream(new BOMInputStream(stream), charsetName)
        .getLines() flatMap stripComment
    )
  }

  private def stripComment(line: String): Option[String] = {
    (line.splitAt(line indexOf '#') match {
      // comments do not exist
      case (take, drop) if take.isEmpty => drop
      // comments exist
      case (take, _) => take
    }).trim match {
      case "" => None
      case x  => Some(x)
    }
  }

  private sealed abstract class Record(regex: Regex) {
    def value(line: String): Option[String] =
      regex.findFirstMatchIn(line).map(_.group(1)).filterNot("" == _)
  }
  private object Record {
    case object UserAgent extends Record("(?i)^user-agent:\\s*([^\\t\\n\\x0B\\f\\r]+)\\s*$".r)
    case object Disallow extends Record("(?i)^disallow:\\s*([^\\s]*)\\s*$".r)
    case object Allow extends Record("(?i)^allow:\\s*([^\\s]*)\\s*$".r)
    case object CrawlDelay extends Record("(?i)^crawl-delay:\\s*([^\\s]+)\\s*$".r)
    case object Sitemap extends Record("(?i)^sitemap:\\s*([^\\s]+)\\s*$".r)

    def read(line: String): Option[(Record, String)] = Seq(UserAgent, Disallow, Allow, CrawlDelay, Sitemap)
      .flatMap { x => x.value(line).map(x -> _) }
      .headOption

  }

}

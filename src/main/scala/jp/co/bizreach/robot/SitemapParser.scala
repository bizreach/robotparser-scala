package jp.co.bizreach.robot

import java.io.{BufferedInputStream, InputStream}
import java.util.zip.GZIPInputStream

import scala.io.Source
import scala.xml.{NodeSeq, XML}

/**
 * The object of parsing the sitemap.
 */
object SitemapParser {
  /**
   * Parse the specified sitemap file.
   *
   * @param stream input stream of sitemap.xml or sitemap text file or gz file
   * @param charsetName encoding (optional: defaults to UTF-8)
   * @return the contents of the parsed sitemap file
   */
  def parse(stream: InputStream, charsetName: String = "UTF-8"): Sitemap = {
    val in = new BufferedInputStream(stream)
    in.mark(512)

    val bytes = new Array[Byte](512)
    in.read(bytes, 0, 512)
    val head = new String(bytes, charsetName)
    in.reset()

    head match {
      // XML Sitemap
      case line if line contains "<urlset" =>
        Urlset(
          (XML.load(in) \ "url").map { url =>
            Urlset.Url(
              loc        = (url \ "loc")       .text,
              lastmod    = (url \ "lastmod")   .textOption,
              changefreq = (url \ "changefreq").textOption,
              priority   = (url \ "priority")  .textOption
            )
          }
        )
      // XML Sitemap Index
      case line if line contains "<sitemapindex" =>
        Sitemapindex(
          (XML.load(in) \ "sitemap").map { url =>
            Sitemapindex.Sitemap(
              loc     = (url \ "loc")    .text,
              lastmod = (url \ "lastmod").textOption
            )
          }
        )
      // Text Sitemap
      case line if line.startsWith("http") || line.startsWith("https") =>
        Urlset(
          Source.fromInputStream(in, charsetName).getLines()
            .withFilter(_ matches "^https?://.*")
            .map { url => Urlset.Url(url) }
            .toSeq
        )
      // gz
      case _ => parse(new GZIPInputStream(in), charsetName)
    }
  }

  implicit class RichNodeSeq(nodeSeq: NodeSeq) {
    def textOption: Option[String] = {
      val text = nodeSeq.text
      if (text == null || text.length == 0) None else Some(text)
    }
  }

}

package jp.co.bizreach.robot

import com.softwaremill.quicklens._

case class RobotsTxt(directive: Seq[Directive] = Nil, sitemap: Seq[String] = Nil) {

  def +=(userAgent: String): RobotsTxt = {
    if(directive.exists(_.userAgent == userAgent))
      this
    else
      this.modify(_.directive).using(Directive(userAgent) +: _)
  }

  def update(userAgent: String*)(mod: Directive => Directive): RobotsTxt = {
    this.modify(_.directive.each).using( x =>
      if(userAgent contains x.userAgent) mod(x) else x
    )
  }

  def find(userAgent: String): Option[Directive] = {
    directive
      .filter (x => s"""(?i)${x.userAgent.replace("*", ".*")}""".r.findFirstIn(userAgent).isDefined)
      .sortBy (_.userAgent.length)
      .lastOption
  }

}

case class Directive(
  userAgent: String,
  disallow: Seq[String] = Nil,
  allow: Seq[String]    = Nil,
  crawlDelay: Int       = 0
)

package jp.co.bizreach.robot

import java.io.ByteArrayInputStream

import org.scalatest._

class RobotsTxtParserSpec extends FunSuite with Matchers {

  private def inputStream(str: String) = new ByteArrayInputStream(str.getBytes)

  test("a single directive"){
    val in = inputStream(
      """User-agent: *
        |Crawl-delay: 5
        |Disallow: /test/
        |Disallow: /help        # disallows /help/index.html etc.
        |Allow: /help/faq.html
        |
        |Sitemap: http://www.example.com/sitemap.xml""".stripMargin)
    val result = RobotsTxtParser.parse(in)

    assert(result.directive.size == 1)
    assert(result.sitemap.size == 1)

    val directive = result.directive.head
    assert(directive.userAgent == "*")
    assert(directive.crawlDelay == 5)
    directive.disallow should contain theSameElementsAs Seq("/test/", "/help")
    directive.allow should contain theSameElementsAs Seq("/help/faq.html")

    val sitemap = result.sitemap.head
    assert(sitemap == "http://www.example.com/sitemap.xml")
  }

  test("multiple directives"){
    val in = inputStream(
      """User-agent: a
        |Disallow: /d
        |
        |User-agent: b
        |Disallow: /e
        |
        |User-agent: c
        |Disallow: /f""".stripMargin)
    val result = RobotsTxtParser.parse(in)

    assert(result.directive.size == 3)
    assert(result.sitemap.size == 0)

    assert(result.directive(0).userAgent == "c")
    assert(result.directive(1).userAgent == "b")
    assert(result.directive(2).userAgent == "a")
    result.directive(0).disallow should contain theSameElementsAs Seq("/f")
    result.directive(1).disallow should contain theSameElementsAs Seq("/e")
    result.directive(2).disallow should contain theSameElementsAs Seq("/d")
  }

  test("ignore invalid Crawl-delay directive"){
    val in = inputStream(
      """User-agent: *
        |Crawl-delay: 5a""".stripMargin)
    val result = RobotsTxtParser.parse(in)

    assert(result.directive.size == 1)

    val directive = result.directive.head
    assert(directive.userAgent == "*")
    assert(directive.crawlDelay == 0)
  }

}

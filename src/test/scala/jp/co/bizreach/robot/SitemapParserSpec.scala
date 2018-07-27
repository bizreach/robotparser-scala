package jp.co.bizreach.robot

import java.io.ByteArrayInputStream

import org.scalatest._

class SitemapParserSpec extends FunSuite with Matchers {

  private def inputStream(str: String) = new ByteArrayInputStream(str.getBytes)

  test("XML sitemap"){
    val in = inputStream(
      """<?xml version="1.0" encoding="UTF-8"?>
        |
        |<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
        |  <url>
        |    <loc>http://www.example.com/</loc>
        |    <lastmod>2018-07-03</lastmod>
        |    <changefreq>weekly</changefreq>
        |    <priority>0.6</priority>
        |  </url>
        |</urlset>""".stripMargin)
    val result = SitemapParser.parse(in)

    result shouldBe a [Urlset]

    val urlset = result.asInstanceOf[Urlset]
    assert(urlset.url.size == 1)

    val url = urlset.url.head
    assert(url.loc == "http://www.example.com/")
    url.lastmod should be (Some("2018-07-03"))
    url.changefreq should be (Some("weekly"))
    url.priority should be (Some("0.6"))
  }

  test("XML sitemap index"){
    val in = inputStream(
      """<?xml version="1.0" encoding="UTF-8"?>
        |
        |<sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
        |  <sitemap>
        |    <loc>http://www.example.com/sitemap1.xml.gz</loc>
        |    <lastmod>2018-07-03</lastmod>
        |  </sitemap>
        |</sitemapindex>""".stripMargin)
    val result = SitemapParser.parse(in)

    result shouldBe a [Sitemapindex]

    val index = result.asInstanceOf[Sitemapindex]
    assert(index.sitemap.size == 1)

    val sitemap = index.sitemap.head
    assert(sitemap.loc == "http://www.example.com/sitemap1.xml.gz")
    sitemap.lastmod should be (Some("2018-07-03"))
  }

  test("Text sitemap"){
    val in = inputStream(
      """http://www.example.com/fashion/page1.html
        |https://www.example.com/beauty/page1.html""".stripMargin)
    val result = SitemapParser.parse(in)

    result shouldBe a [Urlset]

    val urlset = result.asInstanceOf[Urlset]
    assert(urlset.url.size == 2)

    assert(urlset.url(0).loc == "http://www.example.com/fashion/page1.html")
    assert(urlset.url(1).loc == "https://www.example.com/beauty/page1.html")
  }

  test("sitemap.xml.gz"){
    val in = Thread.currentThread().getContextClassLoader.getResourceAsStream("sitemap.xml.gz")
    val result = SitemapParser.parse(in)

    result shouldBe a [Urlset]

    val urlset = result.asInstanceOf[Urlset]
    assert(urlset.url.size == 1)

    val url = urlset.url.head
    assert(url.loc == "http://www.example.com/")
    url.lastmod should be (Some("2018-07-03"))
    url.changefreq should be (Some("weekly"))
    url.priority should be (Some("0.6"))
  }

}

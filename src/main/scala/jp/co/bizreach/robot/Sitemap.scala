package jp.co.bizreach.robot

sealed abstract class Sitemap

case class Urlset(url: Seq[Urlset.Url]) extends Sitemap
case class Sitemapindex(sitemap: Seq[Sitemapindex.Sitemap]) extends Sitemap

object Urlset {
  case class Url(
    /*
     * URL of the page. This URL must begin with the protocol (such as http) and
     * end with a trailing slash, if your web server requires it. This value
     * must be less than 2,048 characters.
     */
    loc: String,
    /*
     * The date of last modification of the file. This date should be in W3C
     * Datetime format. This format allows you to omit the time portion, if
     * desired, and use YYYY-MM-DD.
     * Note that this tag is separate from the If-Modified-Since (304) header
     * the server can return, and search engines may use the information from
     * both sources differently.
     */
    lastmod: Option[String] = None,
    /*
     * How frequently the page is likely to change. This value provides general
     * information to search engines and may not correlate exactly to how often
     * they crawl the page. Valid values are:
     * `always`
     * `hourly`
     * `daily`
     * `weekly`
     * `monthly`
     * `yearly`
     * `never`
     * The value "always" should be used to describe documents that change each
     * time they are accessed. The value "never" should be used to describe
     * archived URLs.
     * Please note that the value of this tag is considered a hint and not a
     * command. Even though search engine crawlers may consider this information
     * when making decisions, they may crawl pages marked "hourly" less
     * frequently than that, and they may crawl pages marked "yearly" more
     * frequently than that. Crawlers may periodically crawl pages marked
     * "never" so that they can handle unexpected changes to those pages.
     */
    changefreq: Option[String] = None,
    /*
     * The priority of this URL relative to other URLs on your site. Valid
     * values range from 0.0 to 1.0. This value does not affect how your pages
     * are compared to pages on other sites—it only lets the search engines know
     * which pages you deem most important for the crawlers.
     *
     * The default priority of a page is 0.5.
     *
     * Please note that the priority you assign to a page is not likely to
     * influence the position of your URLs in a search engine's result pages.
     * Search engines may use this information when selecting between URLs on
     * the same site, so you can use this tag to increase the likelihood that
     * your most important pages are present in a search index.
     * Also, please note that assigning a high priority to all of the URLs on
     * your site is not likely to help you. Since the priority is relative, it
     * is only used to select between URLs on your site.
     */
    priority: Option[String] = None
  )

}

object Sitemapindex {
  case class Sitemap(
    /*
     * Identifies the location of the Sitemap. This location can be a Sitemap,
     * an Atom file, RSS file or a simple text file.
     */
    loc: String,
    /*
     * Identifies the time that the corresponding Sitemap file was modified. It
     * does not correspond to the time that any of the pages listed in that
     * Sitemap were changed. The value for the lastmod tag should be in W3C
     * Datetime format.
     * By providing the last modification timestamp, you enable search engine
     * crawlers to retrieve only a subset of the Sitemaps in the index i.e. a
     * crawler may only retrieve Sitemaps that were modified since a certain
     * date. This incremental Sitemap fetching mechanism allows for the rapid
     * discovery of new URLs on very large sites.
     */
    lastmod: Option[String] = None
  )
}

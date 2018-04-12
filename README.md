# robotparser-scala
robotparser-scala implements a parser for the `robots.txt` file format in Scala.

## Setup

Add robotparser-scala as a dependency in `build.sbt`:

```scala
libraryDependencies += "jp.co.bizreach" %% "robotparser-scala" % "0.0.4"
```

## Usage

You'll parse the `robots.txt` file as following:

```scala
import jp.co.bizreach.robot._

val stream: InputStream = ...
val robotsTxt = RobotsTxtParser.parse(stream)
```

And then, you have `RobotsTxt` instance. By default, character encoding is UTF-8.

If you'll parse the sitemap file, as following:

```scala
import jp.co.bizreach.robot._

val stream: InputStream = ...
SitemapParser.parse(stream) match {
  // Sitemap file
  case x: Urlset => ...

  // Sitemap Index file
  case x: Sitemapindex => ...
}
```

`SitemapParser` supports following files:

- XML Sitemap
- XML Sitemap Index
- Text Sitemap
- gz

And then, you have `Urlset` or `Sitemapindex` instance. By default, character encoding is UTF-8.

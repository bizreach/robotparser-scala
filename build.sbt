name := "robotparser-scala"
organization := "jp.co.bizreach"
version := "0.0.1"
scalaVersion := "2.11.7"
libraryDependencies ++= Seq(
  "org.scalaz"                 %% "scalaz-core" % "7.1.3",
  "com.softwaremill.quicklens" %% "quicklens"   % "1.4.0",
  "commons-io"                 %  "commons-io"  % "2.4"
)
publishMavenStyle := true
publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
  else                             Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
scalacOptions := Seq("-deprecation")
publishArtifact in Test := false
pomIncludeRepository := { _ => false }
pomExtra := (
  <url>https://github.com/bizreach/robotparser-scala</url>
    <licenses>
      <license>
        <name>The Apache Software License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      </license>
    </licenses>
    <scm>
      <url>https://github.com/bizreach/robotparser-scala</url>
      <connection>scm:git:https://github.com/bizreach/robotparser-scala.git</connection>
    </scm>
    <developers>
      <developer>
        <id>shimamoto</id>
        <name>Takako Shimamoto</name>
        <email>takako.shimamoto_at_bizreach.co.jp</email>
        <timezone>+9</timezone>
      </developer>
    </developers>)

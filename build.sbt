name := "robotparser-scala"
organization := "jp.co.bizreach"
scalaVersion := "2.12.8"
crossScalaVersions := Seq(scalaVersion.value, "2.13.0")

libraryDependencies ++= Seq(
  "org.scala-lang.modules"     %% "scala-xml"   % "1.2.0",
  "com.softwaremill.quicklens" %% "quicklens"   % "1.4.12",
  "commons-io"                 %  "commons-io"  % "2.6",
  "org.scalatest"              %% "scalatest"   % "3.0.8" % "test"
)

scalacOptions := Seq("-deprecation", "-feature")

pomExtra := (
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
  </developers>
)
pomIncludeRepository := { _ => false }
publishMavenStyle := true
publishTo := sonatypePublishTo.value
homepage := Some(url(s"https://github.com/bizreach/robotparser-scala"))
licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

sonatypeProfileName := organization.value
releasePublishArtifactsAction := PgpKeys.publishSigned.value
releaseTagName := (version in ThisBuild).value
releaseCrossBuild := true

import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  publishArtifacts,
  releaseStepCommand("sonatypeRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

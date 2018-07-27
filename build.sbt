name := "robotparser-scala"
organization := "jp.co.bizreach"
scalaVersion := "2.12.5"
libraryDependencies ++= Seq(
  "org.scala-lang.modules"     %% "scala-xml"   % "1.1.0",
  "com.softwaremill.quicklens" %% "quicklens"   % "1.4.11",
  "commons-io"                 %  "commons-io"  % "2.6",
  "org.scalatest"              %% "scalatest"   % "3.0.5" % "test"
)
scalacOptions := Seq("-deprecation")

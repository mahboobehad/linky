name := "linky"

version := "0.1"

scalaVersion := "2.13.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val slickV = "3.3.2"
  Seq(
    "com.typesafe.akka" %% "akka-http" % "10.1.11",
    "com.typesafe.akka" %% "akka-stream" % "2.5.26",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.11",
    "org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
    "com.typesafe.slick" %% "slick" % slickV,
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
    "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
    "net.debasishg" %% "redisclient" % "3.20"
  )
}

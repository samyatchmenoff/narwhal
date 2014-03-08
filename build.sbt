organization  := "narwhal"

version       := "0.1"

scalaVersion  := "2.10.3"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= Seq(
  "com.typesafe.akka"   %% "akka-actor"      % "2.1.4",
  "com.typesafe.akka"   %% "akka-testkit"    % "2.1.4",
  "com.typesafe.slick"  %% "slick"           % "2.0.0",
  "org.scalatest"       %% "scalatest"       % "2.1.0"         % "test",
  "ch.qos.logback"      %  "logback-classic" % "0.9.28"        % "test",
  "postgresql"          %  "postgresql"      % "9.1-901.jdbc4" % "test"
)

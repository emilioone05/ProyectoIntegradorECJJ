ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.4"


lazy val root = (project in file("."))
  .settings(
    name := "csvPractica",
    libraryDependencies ++= Seq(
      "com.github.tototoshi"  %%  "scala-csv"         % "2.0.0",
      "org.tpolecat"          %%  "doobie-core"       % "1.0.0-RC5",
      "org.tpolecat"          %%  "doobie-hikari"     % "1.0.0-RC5",
      "com.mysql"             %   "mysql-connector-j" % "9.1.0",
      "org.playframework" %% "play-json" % "3.0.4"
    )
  )

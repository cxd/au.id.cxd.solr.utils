
version := "0.1"

scalaVersion := "2.13.4"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings)
  .settings(
    name := "au.id.cxd.solr.utils",
    maintainer := "cpdavey@cgmail.com",
    mainClass in Compile := Some("au.id.cxd.solr.utils.console.LoadDirectory"),
    libraryDependencies ++= Seq(
      "org.scalactic" %% "scalactic" % "3.2.3",
      "org.scalatest" %% "scalatest" % "3.2.3" % "test",


      "com.github.pathikrit" %% "better-files" % "3.9.1",
      "org.apache.solr" % "solr-solrj" % "8.7.0",
      "com.typesafe" % "config" % "1.4.1",

      "com.github.scopt" %% "scopt" % "4.0.0"

    )
  )
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(UniversalPlugin)

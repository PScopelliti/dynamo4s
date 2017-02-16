import com.typesafe.sbt.SbtGit.{GitKeys => git}

name := "dynamo4s"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/", Resolver.jcenterRepo
)

libraryDependencies ++= {

  val awsSdk = "1.11.68"
  val finagle = "6.40.0"
  val scalaTest = "3.0.1"
  val mockito = "1.10.19"

  Seq(

    // Aws SDK
    "com.amazonaws" % "aws-java-sdk" % awsSdk,

    // Finagle
    "com.twitter" % "finagle-core_2.11" % finagle,
    "com.twitter" % "finagle-http_2.11" % finagle,

    // Scalatest
    "org.scalactic" % "scalactic_2.11" % scalaTest,
    "org.scalatest" % "scalatest_2.11" % scalaTest,

    // Mockito
    "org.mockito" % "mockito-all" % mockito % Test
  )
}


lazy val standardSettings = Seq(

  organization := "com.pscopelliti",
  licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html")),

  homepage := Some(url("https://github.com/PScopelliti/dynamo4s")),

  scmInfo := Some(
    ScmInfo(
      url("https://github.com/PScopelliti/dynamo4s"),
      "scm:git:git@github.com:PScopelliti/dynamo4s.git")
  ),

  apiURL := Some(url("http://pscopelliti.github.io/dynamo4s/latest/api/")),

  crossScalaVersions := Seq("2.12.1", "2.11.8"),

  pomExtra := (
    <developers>
      <developer>
        <id>PScopelliti</id>
        <name>Giuseppe Scopelliti</name>
        <url>http://peppescopelliti.com/</url>
      </developer>
    </developers>
    ),

  publishMavenStyle := true,

  git.gitRemoteRepo := "git@github.com:PScopelliti/dynamo4s.git",

  scalacOptions ++= Seq(
    "-encoding",
    "UTF-8",
    "-Xlint",
    "-deprecation",
    "-Xfatal-warnings",
    "-feature",
    "-language:postfixOps",
    "-unchecked"
  ),

  scalacOptions in(Compile, doc) ++= Seq("-sourcepath", baseDirectory.value.getAbsolutePath),

  autoAPIMappings := true,

  apiURL := Some(url("http://pscopelliti.github.io/dynamodb4s/")),

  scalacOptions in(Compile, doc) ++= {

    val branch = if (version.value.trim.endsWith("SNAPSHOT")) "master" else version.value

    Seq(
      "-doc-source-url",
      "https://github.com/PScopelliti/dynamo4s/tree/" + branch + "€{FILE_PATH}.scala"
    )
  }
)

lazy val coverageSettings = Seq(

  coverageMinimum := 70
)

lazy val root = Project(

  id = "dynamo4s",

  base = file("."),

  settings =
    standardSettings ++
      coverageSettings ++
      site.settings ++
      site.includeScaladoc(version + "/api") ++
      site.includeScaladoc("latest/api") ++
      ghpages.settings
)
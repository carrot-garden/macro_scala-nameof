import com.typesafe.sbt.pgp.PgpKeys

lazy val sharedSettings = Seq(
  organization := "com.github.dwickern",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.6", scalaVersion.value, "2.12.1"),
  libraryDependencies ++= Seq(
  "org.scalatest" %%% "scalatest" % "3.0.0" % "test",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ),
  pomExtra := {
    <url>https://github.com/dwickern/scala-nameof</url>
    <licenses>
      <license>
        <name>MIT license</name>
        <url>http://www.opensource.org/licenses/mit-license.php</url>
      </license>
    </licenses>
    <scm>
      <connection>scm:git:github.com/dwickern/scala-nameof.git</connection>
      <developerConnection>scm:git:git@github.com:dwickern/scala-nameof.git</developerConnection>
      <url>github.com/dwickern/scala-nameof.git</url>
    </scm>
    <developers>
      <developer>
        <id>dwickern</id>
        <name>Derek Wickern</name>
        <url>https://github.com/dwickern</url>
      </developer>
    </developers>
  }
)

releaseCrossBuild := true
releaseProcess := {
  import ReleaseTransformations._
  Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    ReleaseStep(action = Command.process("publishSigned", _), enableCrossBuild = true),
    setNextVersion,
    commitNextVersion,
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _), enableCrossBuild = true),
    pushChanges
  )
}

lazy val root = project.in(file("."))
  .aggregate(nameofJVM, nameofJS)
  .settings(sharedSettings: _*)
  .settings(
    publish := {},
    publishLocal := {},
    PgpKeys.publishSigned := {}
  )

lazy val nameof = crossProject.crossType(CrossType.Pure).in(file("."))
  .settings(sharedSettings: _*)
  .settings(name := "scala-nameof")

lazy val nameofJVM = nameof.jvm.settings(name := "scala-nameof-jvm")
lazy val nameofJS = nameof.js.settings(name := "scala-nameof-js")

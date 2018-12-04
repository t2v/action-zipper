val _version = "0.2.0"

val _crossScalaVersions = Seq("2.12.7", "2.11.12")

val _org = "jp.t2v"

lazy val _publishMavenStyle = true
lazy val _publishArtifactInTest = false
lazy val _pomIncludeRepository = { _: MavenRepository => false }
lazy val _publishTo = { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

lazy val _pomExtra = {
  <url>https://github.com/t2v/action-zipper</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:gakuzzzz/action-zipper.git</url>
      <connection>scm:git:git@github.com:t2v/action-zipper.git</connection>
    </scm>
    <developers>
      <developer>
        <id>gakuzzzz</id>
        <name>gakuzzzz</name>
        <url>https://github.com/gakuzzzz</url>
      </developer>
    </developers>
}

lazy val _scalacOptions = Seq(
  "-feature",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

lazy val root = (project in file(".")).
  aggregate(core, sample).
  settings(
    aggregate in update := false,
    scalaVersion        := "2.12.7",
    crossScalaVersions  := _crossScalaVersions,
    publish             := { },
    publishArtifact     := false,
    packagedArtifacts   := Map.empty,
    publishTo           := _publishTo(_version),
    pomExtra            := _pomExtra
  )

lazy val core = (project in file("core")).
  settings(
    organization := _org,
    name := "action-zipper",
    version := _version,
    scalaVersion := "2.12.7",
    crossScalaVersions := _crossScalaVersions,
    scalacOptions ++= _scalacOptions,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.typesafeRepo("releases")
    ),
    libraryDependencies ++=
      ("com.typesafe.play"  %% "play"      % "2.6.20"  %   "provided") ::
//      ("com.chuusai"        %% "shapeless" % "2.0.0"                ) ::
    Nil,
    (sourceGenerators in Compile) += task[Seq[File]] {
      val dir = (sourceManaged in Compile).value / "jp" / "t2v" / "lab" / "play2" / "actzip"
      (2 to 22).map { i =>
        val action = ActionGenerator(i, 22)
        val file = dir / s"ZippedAction$i.scala"
        IO.write(file, action)
        file
      }
    },
    publishMavenStyle       := _publishMavenStyle,
    publishArtifact in Test := _publishArtifactInTest,
    pomIncludeRepository    := _pomIncludeRepository,
    publishTo               := _publishTo(_version),
    pomExtra                := _pomExtra
  )

lazy val sample = (project in file("sample")).
  dependsOn(core).
  enablePlugins(PlayScala).
  settings(
    scalaVersion := "2.12.7",
    scalacOptions ++= _scalacOptions,
    crossScalaVersions := _crossScalaVersions,
    libraryDependencies ++=
      guice ::
      ("org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test") ::
      Nil,
    publish           := { },
    publishArtifact   := false,
    packagedArtifacts := Map.empty,
    publishTo         := _publishTo(_version),
    pomExtra          := _pomExtra
  )


val _version = "0.1.0-SNAPSHOT"

val _crossScalaVersions = Seq("2.11.6", "2.10.4")

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
    crossScalaVersions  := _crossScalaVersions,
    publish             := { },
    publishArtifact     := false,
    packagedArtifacts   := Map.empty,
    publishTo           <<=(version)(_publishTo),
    pomExtra            := _pomExtra
  )

lazy val core = (project in file("core")).
  settings(
    organization := _org,
    name := "action-zipper",
    version := _version,
    crossScalaVersions := _crossScalaVersions,
    scalacOptions ++= _scalacOptions,
    resolvers ++= Seq(
      Resolver.sonatypeRepo("releases"),
      Resolver.typesafeRepo("releases"),
      "bintray/non" at "http://dl.bintray.com/non/maven"
    ),
    libraryDependencies ++=
      ("com.typesafe.play"  %% "play"      % "2.3.7"  %   "provided") ::
//      ("com.chuusai"        %% "shapeless" % "2.0.0"                ) ::
    Nil,
    (sourceGenerators in Compile) += task[Seq[File]] {
      val dir = (sourceManaged in Compile).value / "jp" / "t2v" / "lab" / "play2" / "actzip"
      val requests = dir / "requests.scala"
      IO.write(requests, RequestGenerator())
      requests +: (2 to 21).map { i =>
        val action = ActionGenerator(i, 21)
        val file = dir / s"ZippedAction$i.scala"
        IO.write(file, action)
        file
      }
    },
    publishMavenStyle       := _publishMavenStyle,
    publishArtifact in Test := _publishArtifactInTest,
    pomIncludeRepository    := _pomIncludeRepository,
    publishTo               <<=(version)(_publishTo),
    pomExtra                := _pomExtra,
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.5.2")
  )

lazy val sample = (project in file("sample")).
  dependsOn(core).
  enablePlugins(PlayScala).
  settings(
    scalacOptions ++= _scalacOptions,
    crossScalaVersions := _crossScalaVersions,
    libraryDependencies ++=
      ("com.h2database"           %  "h2"                                  % "1.4.+") ::
      ("ch.qos.logback"           %  "logback-classic"                     % "1.1.+") ::
      ("jp.t2v"                  %%  "play2-auth"                          % "0.13.0") ::
      ("org.scalikejdbc"         %% "scalikejdbc"                          % "2.2.4") ::
      ("org.scalikejdbc"         %% "scalikejdbc-config"                   % "2.2.4") ::
      ("org.scalikejdbc"         %% "scalikejdbc-syntax-support-macro"     % "2.2.4") ::
      ("org.scalikejdbc"         %% "scalikejdbc-test"                     % "2.2.4"   % "test") ::
      ("org.scalikejdbc"         %% "scalikejdbc-play-plugin"              % "2.3.6") ::
      ("org.scalikejdbc"         %% "scalikejdbc-play-fixture-plugin"      % "2.3.6") ::
      ("com.github.tototoshi"    %% "play-flyway"                          % "1.2.1") ::
      Nil,
    publish           := { },
    publishArtifact   := false,
    packagedArtifacts := Map.empty,
    publishTo         <<=(version)(_publishTo),
    pomExtra          := _pomExtra
  )


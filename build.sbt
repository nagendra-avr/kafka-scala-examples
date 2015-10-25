name := "kafka-scala-examples"

version := "1.0"

scalaVersion := "2.10.4"

libraryDependencies += "org.apache.kafka" % "kafka_2.10" % "0.8.2.2" excludeAll(
  ExclusionRule(organization = "com.sun.jdmk"),
  ExclusionRule(organization = "com.sun.jmx"),
  ExclusionRule(organization = "javax.jms")
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

libraryDependencies += "org.mockito" % "mockito-all" % "1.9.5" % "test"


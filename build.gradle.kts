import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val kmongo_version: String by project
val koin_version: String by project

plugins {
    kotlin("jvm") version "1.8.21"
    id("io.ktor.plugin") version "2.3.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.21"
    id("com.github.johnrengelman.shadow") version "5.2.0"  // shadow plugin generates the jar file
}

group = "com.romanhruska"
version = "0.0.1"
application {
    mainClass.set("io.ktor.server.netty.EngineMain")
    project.setProperty("mainClassName", mainClass.get())  // this is needed by shadow plugin

/*    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")*/
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

val sshAntTask = configurations.create("sshAntTask")

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Kmongo
    //TODO get rid of mongo
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")

    // Koin core features
    implementation("io.insert-koin:koin-core:$koin_version") // had to be 3.4.0 in order to follow the init...
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    sshAntTask("org.apache.ant:ant-jsch:1.9.2")

    // ktorm
    implementation("org.ktorm:ktorm-core:3.5.0")
    implementation("org.ktorm:ktorm-support-mysql:3.5.0")
}

//for creation of JAR file
tasks.withType<ShadowJar> {
    manifest {
        attributes(
            "Main-Class" to application.mainClass.get()
        )
    }
    archiveFileName.set("com.romanhruska.ktor-pl-chat-${version}-all.jar")
}


ant.withGroovyBuilder {
    "taskdef"(
        "name" to "scp",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
        "classpath" to configurations.get("sshAntTask").asPath
    )
    "taskdef"(
        "name" to "ssh",
        "classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
        "classpath" to configurations.get("sshAntTask").asPath
    )
}

task("deploy") {
    dependsOn("clean", "shadowJar")   //it will 1st clean our project and then generate Jar file
    ant.withGroovyBuilder {
        doLast {
            val knownHosts = File.createTempFile("knownhosts","txt")
            val user = "ubuntu"
            val host = "130.61.29.151"
            val pk = file("keys/oracle-ssh")  //private key
            //var jarFileName = "com.romanhruska.ktor-pl-chat-all.jar"
            var jarFileName = "com.romanhruska.ktor-pl-chat-$version-all.jar"
            try {
                "scp"(
                    "file" to file("build/libs/$jarFileName"),
                    "todir" to "$user@$host:/root/chat",
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "mv /root/chat/$jarFileName /root/chat/chat-server.jar"  // rename jar file name to chat-server.jar
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "sudo systemctl stop chat"   // stop service
                )
                "ssh"(
                    "host" to host,
                    "username" to user,
                    "keyfile" to pk,
                    "trust" to true,
                    "knownhosts" to knownHosts,
                    "command" to "sudo systemctl start chat" // start service
                )
            } finally {
                knownHosts.delete()
            }
        }
    }
}
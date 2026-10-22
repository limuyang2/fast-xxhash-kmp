import org.gradle.api.publish.maven.tasks.GenerateMavenPom
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    `maven-publish`
    signing
}

android {
    namespace = "io.github.limuyang2.xxhash.lib.internal"
    compileSdk = 37

    defaultConfig {
        minSdk = 21

        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    externalNativeBuild {
        cmake {
            path("CMakeLists.txt")
            version = "3.22.1"
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}


//---------- maven upload info -----------------------------------

var signingKeyId = "" //签名的密钥后8位
var signingPassword = "" //签名设置的密码
var secretKeyRingFile = "" //生成的secring.gpg文件目录

val localProperties: File = project.rootProject.file("local.properties")

if (localProperties.exists()) {
    println("Found secret props file, loading props")
    val properties = Properties()

    InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
        properties.load(reader)
    }
    signingKeyId = properties.getProperty("signing.keyId")
    signingPassword = properties.getProperty("signing.password")
    secretKeyRingFile = properties.getProperty("signing.secretKeyRingFile")
} else {
    println("No props file, loading env vars")
}

val versionName = providers.gradleProperty("XXHASH_NATIVE_VERSION").get()

group = "io.github.limuyang2"
version = versionName

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components.findByName("release"))
                groupId = "io.github.limuyang2"
                artifactId = "xxhash-android-native"
                version = versionName

                pom {
                    name.value("xxhash-android-native")
                    description.value("Android native JNI artifacts for xxhash.")
                    url.value("https://github.com/limuyang2/fast-xxhash-kmp")

                    licenses {
                        license {
                            name.value("The MIT License")
                            url.value("https://github.com/limuyang2/fast-xxhash-kmp/blob/main/LICENSE")
                        }
                    }

                    developers {
                        developer {
                            id.value("limuyang2")
                            name.value("limuyang")
                            email.value("limuyang2@hotmail.com")
                        }
                    }

                    scm {
                        connection.value("scm:git@github.com:limuyang2/fast-xxhash-kmp.git")
                        developerConnection.value("scm:git@github.com:limuyang2/fast-xxhash-kmp.git")
                        url.value("https://github.com/limuyang2/fast-xxhash-kmp")
                    }
                }
            }
        }

        repositories {
            maven {
                name = "CentralStaging"
                setUrl("$rootDir/RepoDir")
            }
        }
    }
}

tasks.register("prepareMavenCentralPublication") {
    group = "publishing"
    description = "Generate POM, sources and javadoc jars locally without signing or uploading"
    dependsOn(tasks.withType<GenerateMavenPom>())
    dependsOn("sourceReleaseJar", "javaDocReleaseJar")
}

gradle.taskGraph.whenReady {
    if (allTasks.any { it is Sign }) {
        allprojects {
            extra["signing.keyId"] = signingKeyId
            extra["signing.secretKeyRingFile"] = secretKeyRingFile
            extra["signing.password"] = signingPassword
        }
    }
}

signing {
    sign(publishing.publications)
}
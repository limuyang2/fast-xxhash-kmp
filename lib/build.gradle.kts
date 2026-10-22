import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    `maven-publish`
    signing
}

val xxhashSourceDir = rootProject.layout.projectDirectory.dir("lib_android_native/src/main/cpp")

fun registerXxhashStaticLibTask(
    targetName: String,
    sdk: String,
    arch: String,
    platformFlag: String,
) = tasks.register<Exec>("buildXxhash${targetName.replaceFirstChar { it.uppercase() }}StaticLib") {
    val outputDir = layout.buildDirectory.dir("native/xxhash/$targetName")
    val objectFile = outputDir.map { it.file("xxhash.o") }
    val libraryFile = outputDir.map { it.file("libxxhash.a") }
    val sourceFile = xxhashSourceDir.file("xxhash.c")

    inputs.file(sourceFile)
    inputs.file(xxhashSourceDir.file("xxhash.h"))
    outputs.file(libraryFile)

    commandLine(
        "sh",
        "-c",
        """
        set -e
        mkdir -p "${outputDir.get().asFile.absolutePath}"
        SDK_PATH="${'$'}(xcrun --sdk $sdk --show-sdk-path)"
        xcrun --sdk $sdk clang \
          -arch $arch \
          -isysroot "${'$'}SDK_PATH" \
          $platformFlag \
          -O3 \
          -c "${sourceFile.asFile.absolutePath}" \
          -o "${objectFile.get().asFile.absolutePath}"
        xcrun --sdk $sdk ar rcs "${libraryFile.get().asFile.absolutePath}" "${objectFile.get().asFile.absolutePath}"
        """.trimIndent()
    )
}

val buildXxhashIosArm64StaticLib = registerXxhashStaticLibTask(
    targetName = "iosArm64",
    sdk = "iphoneos",
    arch = "arm64",
    platformFlag = "-miphoneos-version-min=12.0",
)

val buildXxhashIosSimulatorArm64StaticLib = registerXxhashStaticLibTask(
    targetName = "iosSimulatorArm64",
    sdk = "iphonesimulator",
    arch = "arm64",
    platformFlag = "-mios-simulator-version-min=12.0",
)

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvm()

    js {
        browser()
        nodejs()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        nodejs()
    }
    applyDefaultHierarchyTemplate()

    android {
        namespace = "io.github.limuyang2.xxhash.lib"
        compileSdk = 37
        minSdk = 21

        withHostTest {}

        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        optimization {
            consumerKeepRules.apply {
                publish = true
                file("consumer-rules.pro")
            }
        }

        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }

    iosArm64 {
        compilations.getByName("main") {
            cinterops {
                val xxhash by creating {
                    defFile(project.file("src/nativeInterop/cinterop/xxhash.def"))
                    includeDirs(xxhashSourceDir.asFile)
                    extraOpts(
                        "-libraryPath",
                        layout.buildDirectory.dir("native/xxhash/iosArm64").get().asFile.absolutePath,
                        "-staticLibrary",
                        "libxxhash.a",
                    )
                    tasks.named(interopProcessingTaskName).configure {
                        dependsOn(buildXxhashIosArm64StaticLib)
                    }
                }
            }
        }
    }

    iosSimulatorArm64 {
        compilations.getByName("main") {
            cinterops {
                val xxhash by creating {
                    defFile(project.file("src/nativeInterop/cinterop/xxhash.def"))
                    includeDirs(xxhashSourceDir.asFile)
                    extraOpts(
                        "-libraryPath",
                        layout.buildDirectory.dir("native/xxhash/iosSimulatorArm64").get().asFile.absolutePath,
                        "-staticLibrary",
                        "libxxhash.a",
                    )
                    tasks.named(interopProcessingTaskName).configure {
                        dependsOn(buildXxhashIosSimulatorArm64StaticLib)
                    }
                }
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        androidMain.dependencies {
            implementation(project(":lib_android_native"))
        }

        jvmMain.dependencies {
            implementation(libs.lz4.java)
            implementation(libs.zero.allocation.hashing)
        }

        iosMain.dependencies {
        }

        webMain.dependencies {
        }

        getByName("androidDeviceTest").dependencies {
            implementation(libs.androidx.junit)
            implementation(libs.androidx.espresso.core)
        }
    }
}

//---------- maven upload info -----------------------------------

val versionName = "2.0.1"

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

group = "io.github.limuyang2"
version = versionName

val emptyJavadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    description = "Assembles an empty javadoc jar required by Maven Central validation."
}

val publishWebPublicationsToRepo = tasks.register("publishWebPublicationsToRepo") {
    group = "publishing"
    description = "Publish Kotlin Multiplatform metadata plus JS/WasmJS artifacts to the configured Maven repository."
    dependsOn(
        "publishKotlinMultiplatformPublicationToMavenRepository",
        "publishJsPublicationToMavenRepository",
        "publishWasmJsPublicationToMavenRepository",
    )
}

val publishWebPublicationsToMavenLocal = tasks.register("publishWebPublicationsToMavenLocal") {
    group = "publishing"
    description = "Publish Kotlin Multiplatform metadata plus JS/WasmJS artifacts to Maven Local."
    dependsOn(
        "publishKotlinMultiplatformPublicationToMavenLocal",
        "publishJsPublicationToMavenLocal",
        "publishWasmJsPublicationToMavenLocal",
    )
}

publishing {
    publications.withType<MavenPublication>().configureEach {
        groupId = "io.github.limuyang2"
        version = versionName


        artifactId = when (name) {
            "kotlinMultiplatform" -> "xxhash"
            "android" -> "xxhash-android"
            "jvm" -> "xxhash-jvm".apply { artifact(emptyJavadocJar) }
            "js" -> "xxhash-js"
            "wasmJs" -> "xxhash-wasm-js"
            "iosArm64" -> "xxhash-ios-arm64"
            "iosSimulatorArm64" -> "xxhash-ios-simulator-arm64"
            else -> artifactId
        }

        pom {
            name.value("xxhash")
            description.value("A xxhash Kotlin Multiplatform library.")
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

    repositories {
        maven {
            name = "Maven"
            setUrl("$rootDir/RepoDir")
        }
    }
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

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.compose.multiplatform) apply false
}

val publicationVersion = providers.gradleProperty("XXHASH_VERSION").get()
val nativeVersion = providers.gradleProperty("XXHASH_NATIVE_VERSION").get()

tasks.register("prepareMavenCentralPublication") {
    group = "publishing"
    description = "本地生成全部 POM、源码及文档 JAR，不签名、不上传"
    dependsOn(":lib:prepareMavenCentralPublication")
    dependsOn(":lib_android_native:prepareMavenCentralPublication")
}

tasks.register("stageMavenCentralPublication") {
    group = "publishing"
    description = "构建、签名全部组件并发布到本地 RepoDir，不上传"
    dependsOn(":lib:publishAllPublicationsToCentralStagingRepository")
    dependsOn(":lib_android_native:publishReleasePublicationToCentralStagingRepository")
}

val bundleMavenCentral = tasks.register<Zip>("bundleMavenCentral") {
    group = "publishing"
    description = "将本地签名产物打包为 Central Portal 手动上传 ZIP，不上传"
    dependsOn("stageMavenCentralPublication")
    archiveFileName.set("xxhash-$publicationVersion.zip")
    destinationDirectory.set(layout.projectDirectory.dir("RepoDir"))
    duplicatesStrategy = DuplicatesStrategy.FAIL
    includeEmptyDirs = false
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
    from(layout.projectDirectory.dir("RepoDir"))
    include("io/**/$publicationVersion/**")
    include("io/**/$nativeVersion/**")
    exclude("**/maven-metadata.xml*")
}

tasks.register("publishAll") {
    group = "publishing"
    description = "仅在本地构建、签名并打包全部组件，生成手动上传 ZIP"
    dependsOn(bundleMavenCentral)
}

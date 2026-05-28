# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

xxHash Kotlin Multiplatform Library (KMP). Provides one-shot hashing via `XXH32`, `XXH64`, `XXH3_64bits`, `XXH3_128bits` across Android, JVM, iOS, JS, and WasmJS targets. Published to Maven Central as `io.github.limuyang2:xxhash`.

## Architecture

The library uses Kotlin's `expect`/`actual` mechanism. `XXHash` is an `expect object` defined in `commonMain` with platform-specific `actual` implementations:

- **Android** (`lib/src/androidMain`): Delegates to `lib_android_native` JNI module → C implementation via NDK/CMake
- **iOS** (`lib/src/iosMain`): Uses Kotlin/Native cinterop with the same C sources, compiled as static libraries via custom `Exec` tasks in `lib/build.gradle.kts`
- **JVM** (`lib/src/jvmMain`): Backed by `org.lz4:lz4-java` (XXH32/64) and `net.openhft:zero-allocation-hashing` (XXH3)
- **JS/WasmJS** (`lib/src/webMain`): Pure Kotlin implementations in `XXHashWebPure.kt` and `XXH3WebPure.kt`

The public API consists of two files in `commonMain`:
- `XXHash.kt` — `expect object` with low-level `ByteArray`/seed functions
- `XXHashExt.kt` — extension functions on `ByteArray` and `String` (e.g. `"hello".xxh64()`)

## Module Structure

- `:lib` — the published KMP library (all targets)
- `:lib_android_native` — Android-only JNI native module (CMake + C sources in `src/main/cpp/`). Loaded as `System.loadLibrary("muxxhash")`
- `:commonApp` — shared Compose Multiplatform UI demo (Android, iOS, Web)
- `:androidApp` — Android demo app, depends on `:commonApp`
- `:webApp` — JS/WasmJS browser demo app, depends on `:commonApp`
- `:iosApp` — Xcode project for iOS demo

## Build Commands

```bash
# Run tests
./gradlew :lib:jvmTest
./gradlew :lib:iosSimulatorArm64Test
./gradlew :lib:jsNodeTest
./gradlew :lib:wasmJsNodeTest

# Compile checks for web targets
./gradlew :lib:compileKotlinJs
./gradlew :lib:compileKotlinWasmJs

# Demo apps
./gradlew :androidApp:installDebug
./gradlew :webApp:jsBrowserDevelopmentWebpack
./gradlew :webApp:wasmJsBrowserDevelopmentWebpack

# Publish to local RepoDir
./gradlew :lib:publishKotlinMultiplatformPublicationToMavenRepository
```

## Key Details

- Android NDK builds 4 ABIs: `armeabi-v7a`, `arm64-v8a`, `x86`, `x86_64`
- iOS static libs are built with `xcrun clang -O3` targeting iOS 12.0+
- C sources (`xxhash.c`, `xxhash.h`) live in `lib_android_native/src/main/cpp/` and are shared between Android JNI and iOS cinterop
- The `lib` build script (`lib/build.gradle.kts`) registers custom `Exec` tasks (`buildXxhashIosArm64StaticLib`, `buildXxhashIosSimulatorArm64StaticLib`) that compile the C sources for iOS before cinterop runs
- Version is hardcoded in `lib/build.gradle.kts` (`versionName = "2.0.1"`)
- Signing credentials for Maven publishing are read from `local.properties`
- Kotlin 2.3.21, Compose Multiplatform 1.11.0, AGP 9.2.1

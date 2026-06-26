# XXHash Kotlin Multiplatform

[![Test](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/test.yml/badge.svg)](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/test.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
[![Context7](https://img.shields.io/badge/Context7-AI-5D5FEC.svg)](https://context7.com/limuyang2/fast-xxhash-kmp)
[![中文文档](https://img.shields.io/badge/%E6%96%87%E6%A1%A3-%E4%B8%AD%E6%96%87-blue)](https://github.com/limuyang2/fast-xxhash-android/blob/main/README_CN.md)
[![zread](https://img.shields.io/badge/Ask_Zread-_.svg?style=flat&color=00b0aa&labelColor=000000&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTQuOTYxNTYgMS42MDAxSDIuMjQxNTZDMS44ODgxIDEuNjAwMSAxLjYwMTU2IDEuODg2NjQgMS42MDE1NiAyLjI0MDFWNC45NjAxQzEuNjAxNTYgNS4zMTM1NiAxLjg4ODEgNS42MDAxIDIuMjQxNTYgNS42MDAxSDQuOTYxNTZDNS4zMTUwMiA1LjYwMDEgNS42MDE1NiA1LjMxMzU2IDUuNjAxNTYgNC45NjAxVjIuMjQwMUM1LjYwMTU2IDEuODg2NjQgNS4zMTUwMiAxLjYwMDEgNC45NjE1NiAxLjYwMDFaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00Ljk2MTU2IDEwLjM5OTlIMi4yNDE1NkMxLjg4ODEgMTAuMzk5OSAxLjYwMTU2IDEwLjY4NjQgMS42MDE1NiAxMS4wMzk5VjEzLjc1OTlDMS42MDE1NiAxNC4xMTM0IDEuODg4MSAxNC4zOTk5IDIuMjQxNTYgMTQuMzk5OUg0Ljk2MTU2QzUuMzE1MDIgMTQuMzk5OSA1LjYwMTU2IDE0LjExMzQgNS42MDE1NiAxMy43NTk5VjExLjAzOTlDNS42MDE1NiAxMC42ODY0IDUuMzE1MDIgMTAuMzk5OSA0Ljk2MTU2IDEwLjM5OTlaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik0xMy43NTg0IDEuNjAwMUgxMS4wMzg0QzEwLjY4NSAxLjYwMDEgMTAuMzk4NCAxLjg4NjY0IDEwLjM5ODQgMi4yNDAxVjQuOTYwMUMxMC4zOTg0IDUuMzEzNTYgMTAuNjg1IDUuNjAwMSAxMS4wMzg0IDUuNjAwMUgxMy43NTg0QzE0LjExMTkgNS42MDAxIDE0LjM5ODQgNS4zMTM1NiAxNC4zOTg0IDQuOTYwMVYyLjI0MDFDMTQuMzk4NCAxLjg4NjY0IDE0LjExMTkgMS42MDAxIDEzLjc1ODQgMS42MDAxWiIgZmlsbD0iI2ZmZiIvPgo8cGF0aCBkPSJNNCAxMkwxMiA0TDQgMTJaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00IDEyTDEyIDQiIHN0cm9rZT0iI2ZmZiIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIvPgo8L3N2Zz4K&logoColor=ffffff)](https://zread.ai/undefined/undefined)

Kotlin Multiplatform Library for [xxHash](https://github.com/Cyan4973/xxHash/) with support for Android, JVM, iOS, JS, and Wasm.

The core library is in `:lib`. This repository also includes demo apps for Android, iOS, and Web, plus a shared Compose UI module.

## Status

- Algorithms: `XXH32`, `XXH64`, `XXH3_64bits`, `XXH3_128bits`
- Targets: Android, JVM, iOS Arm64, iOS Simulator Arm64, JS, WasmJS
- Android uses JNI + the original C implementation
- iOS uses the same original C source implementation as Android, exposed through Kotlin/Native cinterop
- JS and WasmJS currently use a pure Kotlin implementation maintained in `webMain`


## Platform implementation notes

- Android: JNI wrapper over `lib_android_native/src/main/cpp/xxhash.c`
- iOS: same implementation strategy as Android, using the original `xxhash` C sources through Kotlin/Native cinterop
- JVM: JVM-side implementation backed by `org.lz4:lz4-java` and `net.openhft:zero-allocation-hashing`
- Web: pure Kotlin implementation in `lib/src/webMain/kotlin/io/github/limuyang2/xxhash/lib`

The public API is consistent, but the JVM and Web implementations are not exactly the same.

## Installation

[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
```kotlin
dependencies {
    implementation("io.github.limuyang2:xxhash:<latest-version>")
}
```

Maven Central:

- `io.github.limuyang2:xxhash`
- `io.github.limuyang2:xxhash-android`
- `io.github.limuyang2:xxhash-jvm`
- `io.github.limuyang2:xxhash-js`
- `io.github.limuyang2:xxhash-wasm-js`

The root `xxhash` artifact is the normal entry point for KMP projects.

## Usage

### Kotlin

```kotlin
import io.github.limuyang2.xxhash.lib.xxh32
import io.github.limuyang2.xxhash.lib.xxh64
import io.github.limuyang2.xxhash.lib.xxh3As64
import io.github.limuyang2.xxhash.lib.xxh3As128

val text = "Hello, World!"

val h32 = text.xxh32()
val h64 = text.xxh64()
val h3_64 = text.xxh3As64()
val h3_128 = text.xxh3As128() // longArrayOf(low64, high64)

val bytes = text.encodeToByteArray()
val slice = bytes.xxh32(offset = 7, length = 6)
val seeded = bytes.xxh64(seed = 42)
```

## Public API

| Method | Returns | Notes |
| --- | --- | --- |
| `xxh32(input, seed)` | `Long` | 32-bit hash in a Kotlin `Long` |
| `xxh32Bytes(input, offset, length, seed)` | `Long` | slice hashing |
| `xxh64(input, seed)` | `Long` | 64-bit hash |
| `xxh64Bytes(input, offset, length, seed)` | `Long` | slice hashing |
| `xxh3_64bits(input)` | `Long` | XXH3 64-bit |
| `xxh3_64bitsWithSeed(input, seed)` | `Long` | XXH3 64-bit with seed |
| `xxh3_128bits(input)` | `LongArray` | `[low64, high64]` |
| `xxh3_128bitsWithSeed(input, seed)` | `LongArray` | `[low64, high64]` |

All values are exposed as signed Kotlin `Long`. Treat them as unsigned when formatting or comparing against canonical xxHash hex output.

## Demo apps

Android demo:

```bash
./gradlew :androidApp:installDebug
```

iOS demo:

```bash
./gradlew :commonApp:embedAndSignAppleFrameworkForXcode
```

Web demo:

```bash
./gradlew :webApp:jsBrowserDevelopmentWebpack
./gradlew :webApp:wasmJsBrowserDevelopmentWebpack
```

`commonApp` contains the shared Compose UI used by Android, iOS, and Web.

## Build and test

Library compile checks:

```bash
./gradlew :lib:compileKotlinJs
./gradlew :lib:compileKotlinWasmJs
./gradlew :lib:compileTestKotlinJs
```

Selected tests:

```bash
./gradlew :lib:jvmTest
./gradlew :lib:iosSimulatorArm64Test
./gradlew :lib:jsNodeTest
./gradlew :lib:wasmJsNodeTest
```

Some web tasks depend on the local Gradle/Node toolchain and your environment setup.

## Repository structure

Useful paths:

- `lib/src/commonMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/webMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/iosMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/androidMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib_android_native/src/main/cpp`

## License

MIT License

This repository includes [xxHash](https://github.com/Cyan4973/xxHash/) source code. xxHash itself is distributed under the BSD 2-Clause License.

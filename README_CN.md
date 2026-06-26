# XXHash Kotlin Multiplatform

[![Test](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/test.yml/badge.svg)](https://github.com/limuyang2/fast-xxhash-android/actions/workflows/test.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
[![Context7](https://img.shields.io/badge/Context7-AI-5D5FEC.svg)](https://context7.com/limuyang2/fast-xxhash-kmp)
[![zread](https://img.shields.io/badge/Ask_Zread-_.svg?style=flat&color=00b0aa&labelColor=000000&logo=data%3Aimage%2Fsvg%2Bxml%3Bbase64%2CPHN2ZyB3aWR0aD0iMTYiIGhlaWdodD0iMTYiIHZpZXdCb3g9IjAgMCAxNiAxNiIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZD0iTTQuOTYxNTYgMS42MDAxSDIuMjQxNTZDMS44ODgxIDEuNjAwMSAxLjYwMTU2IDEuODg2NjQgMS42MDE1NiAyLjI0MDFWNC45NjAxQzEuNjAxNTYgNS4zMTM1NiAxLjg4ODEgNS42MDAxIDIuMjQxNTYgNS42MDAxSDQuOTYxNTZDNS4zMTUwMiA1LjYwMDEgNS42MDE1NiA1LjMxMzU2IDUuNjAxNTYgNC45NjAxVjIuMjQwMUM1LjYwMTU2IDEuODg2NjQgNS4zMTUwMiAxLjYwMDEgNC45NjE1NiAxLjYwMDFaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00Ljk2MTU2IDEwLjM5OTlIMi4yNDE1NkMxLjg4ODEgMTAuMzk5OSAxLjYwMTU2IDEwLjY4NjQgMS42MDE1NiAxMS4wMzk5VjEzLjc1OTlDMS42MDE1NiAxNC4xMTM0IDEuODg4MSAxNC4zOTk5IDIuMjQxNTYgMTQuMzk5OUg0Ljk2MTU2QzUuMzE1MDIgMTQuMzk5OSA1LjYwMTU2IDE0LjExMzQgNS42MDE1NiAxMy43NTk5VjExLjAzOTlDNS42MDE1NiAxMC42ODY0IDUuMzE1MDIgMTAuMzk5OSA0Ljk2MTU2IDEwLjM5OTlaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik0xMy43NTg0IDEuNjAwMUgxMS4wMzg0QzEwLjY4NSAxLjYwMDEgMTAuMzk4NCAxLjg4NjY0IDEwLjM5ODQgMi4yNDAxVjQuOTYwMUMxMC4zOTg0IDUuMzEzNTYgMTAuNjg1IDUuNjAwMSAxMS4wMzg0IDUuNjAwMUgxMy43NTg0QzE0LjExMTkgNS42MDAxIDE0LjM5ODQgNS4zMTM1NiAxNC4zOTg0IDQuOTYwMVYyLjI0MDFDMTQuMzk4NCAxLjg4NjY0IDE0LjExMTkgMS42MDAxIDEzLjc1ODQgMS42MDAxWiIgZmlsbD0iI2ZmZiIvPgo8cGF0aCBkPSJNNCAxMkwxMiA0TDQgMTJaIiBmaWxsPSIjZmZmIi8%2BCjxwYXRoIGQ9Ik00IDEyTDEyIDQiIHN0cm9rZT0iI2ZmZiIgc3Ryb2tlLXdpZHRoPSIxLjUiIHN0cm9rZS1saW5lY2FwPSJyb3VuZCIvPgo8L3N2Zz4K&logoColor=ffffff)](https://zread.ai/limuyang2/fast-xxhash-android)

面向 Kotlin Multiplatform 的 [xxHash](https://github.com/Cyan4973/xxHash/) 支持库，支持 Android、JVM、iOS、JS、Wasm。

核心库位于 `:lib`。仓库中同时包含 Android、iOS、Web 的示例应用，以及共享的 Compose UI 模块。

## 当前状态

- 算法支持：`XXH32`、`XXH64`、`XXH3_64bits`、`XXH3_128bits`
- 目标平台：Android、JVM、iOS Arm64、iOS Simulator Arm64、JS、WasmJS
- Android 基于 JNI + 原始 C 实现
- iOS 和 Android 一样，直接基于原始 C 源码实现，通过 Kotlin/Native cinterop 暴露
- JS / WasmJS 当前使用 `webMain` 下纯 Kotlin 自行实现的版本

## 平台实现说明

- Android：通过 `lib_android_native/src/main/cpp/xxhash.c` 的 JNI 封装调用原始 C 实现
- iOS：和 Android 完全一致，直接基于同一份 `xxhash` C 源码实现，通过 Kotlin/Native cinterop 暴露
- JVM：基于 `org.lz4:lz4-java` 和 `net.openhft:zero-allocation-hashing`
- Web：使用 `lib/src/webMain/kotlin/io/github/limuyang2/xxhash/lib` 下纯 Kotlin 自行实现的版本

也就是说，公开 API 是一致的，但JVM、Web实现并不完全相同。

## 引入方式

业务项目通常直接依赖：

[![Maven Central](https://img.shields.io/maven-central/v/io.github.limuyang2/xxhash.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.limuyang2/xxhash)
```kotlin
dependencies {
    implementation("io.github.limuyang2:xxhash:<latest-version>")
}
```

Maven Central 上目前包含这些坐标：

- `io.github.limuyang2:xxhash`
- `io.github.limuyang2:xxhash-android`
- `io.github.limuyang2:xxhash-jvm`
- `io.github.limuyang2:xxhash-js`
- `io.github.limuyang2:xxhash-wasm-js`

其中 `xxhash` 是 Kotlin Multiplatform 项目的标准入口。

## 使用示例

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

## 公共 API

| 方法 | 返回值 | 说明 |
| --- | --- | --- |
| `xxh32(input, seed)` | `Long` | 32 位哈希，封装在 Kotlin `Long` 中 |
| `xxh32Bytes(input, offset, length, seed)` | `Long` | 切片哈希 |
| `xxh64(input, seed)` | `Long` | 64 位哈希 |
| `xxh64Bytes(input, offset, length, seed)` | `Long` | 切片哈希 |
| `xxh3_64bits(input)` | `Long` | XXH3 64 位 |
| `xxh3_64bitsWithSeed(input, seed)` | `Long` | 带 seed 的 XXH3 64 位 |
| `xxh3_128bits(input)` | `LongArray` | `[low64, high64]` |
| `xxh3_128bitsWithSeed(input, seed)` | `LongArray` | `[low64, high64]` |

所有返回值都通过有符号 `Long` 暴露。如果你需要和 xxHash 官方十六进制向量对比，请按无符号方式格式化。

## 示例应用

Android 示例：

```bash
./gradlew :androidApp:installDebug
```

iOS 示例：

```bash
./gradlew :commonApp:embedAndSignAppleFrameworkForXcode
```

Web 示例：

```bash
./gradlew :webApp:jsBrowserDevelopmentWebpack
./gradlew :webApp:wasmJsBrowserDevelopmentWebpack
```

`commonApp` 中包含 Android、iOS、Web 共用的 Compose UI。

## 构建与测试

库编译检查：

```bash
./gradlew :lib:compileKotlinJs
./gradlew :lib:compileKotlinWasmJs
./gradlew :lib:compileTestKotlinJs
```

可选测试：

```bash
./gradlew :lib:jvmTest
./gradlew :lib:iosSimulatorArm64Test
./gradlew :lib:jsNodeTest
./gradlew :lib:wasmJsNodeTest
```

部分 web 任务依赖本地 Gradle / Node 工具链和你的环境配置。

## 重要目录

- `lib/src/commonMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/webMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/iosMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib/src/androidMain/kotlin/io/github/limuyang2/xxhash/lib`
- `lib_android_native/src/main/cpp`

## 许可证

MIT License

本仓库包含 [xxHash](https://github.com/Cyan4973/xxHash/) 源码。xxHash 本身采用 BSD 2-Clause License。

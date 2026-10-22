package io.github.limuyang2.xxhash.demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(title = "XXHash Demo (JVM)", onCloseRequest = ::exitApplication) {
        XxHashDemoApp()
    }
}

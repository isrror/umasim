package io.github.mee1080.umasim.compose

import java.awt.Taskbar
import javax.imageio.ImageIO
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.rememberWindowState
import io.github.mee1080.umasim.compose.common.lib.defaultThreadCount
import io.github.mee1080.umasim.mcp.runStdioMcpServer
import io.github.mee1080.utility.localMode

fun main(args: Array<String>) {

    if (args.getOrNull(0) == "mcp") {
        val simulationThreadCount =
            args.getOrNull(1)?.toIntOrNull() ?: defaultThreadCount

        runStdioMcpServer(simulationThreadCount)
        return
    }

    localMode = args.contains("local")

    application {

        runCatching {
            val icon = ImageIO.read(
                object {}.javaClass.getResourceAsStream("/icon.png")
            )

            if (Taskbar.isTaskbarSupported()) {
                Taskbar.getTaskbar().iconImage = icon
            }
        }

        Window(
            onCloseRequest = ::exitApplication,
            title = "赛马娘赛事模拟器移植版",
            state = rememberWindowState(
                width = 900.dp,
                height = 900.dp
            )
        ) {

            val awtWindow = window

            LaunchedEffect(Unit) {
                runCatching {
                    val icon = ImageIO.read(
                        object {}.javaClass.getResourceAsStream("/icon.png")
                    )

                    awtWindow.iconImage = icon
                }
            }

            App()
        }
    }
}
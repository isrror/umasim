package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.mee1080.umasim.compose.common.atoms.TextWithLink

@Composable
fun Header() {
    Row {
        Text("赛马娘赛事模拟器移植版", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun Footer() {
    HorizontalDivider()

    Column {
        Row { Text("注意事项", style = MaterialTheme.typography.headlineSmall) }
        """
        这里只是参考值。实现比较粗糙，数据准确性也不高，请酌情参考。
        与其他赛马娘相关的发动条件，近似成每帧按固定概率触发。某些双次触发技能过多，后续还要修。
        名次条件和其他很多细节都被忽略了。具体请看对应技能的 tooltip。
        各类资料都给了我很大的参考。
    """.trimIndent().split("\n").forEachIndexed { index, c ->
            Row { Text("${index + 1}: ${c.trim()}") }
        }
    }

    HorizontalDivider()

    Column {
        Row { Text("关于本程序", style = MaterialTheme.typography.headlineSmall) }
        TextWithLink(
            listOf(
                "本程序由砂井裏鍵さん（X: " to null,
                "@urakagi" to "https://twitter.com/urakagi",
                "）制作的赛事模拟器，由 mee1080（X: " to null,
                "@mee10801" to "https://twitter.com/mee10801",
                "）移植到 Kotlin" to null,
            )
        )
        TextWithLink("原版：http://race.wf-calc.net/")
    }

    HorizontalDivider()

    Column {
        Row { Text("开源许可", style = MaterialTheme.typography.headlineSmall) }
        TextWithLink("界面显示使用了「LINE Seed JP」字体（https://seed.line.me/index_jp.html）。")
        Text("\"LINE Seed JP\" is licensed under the SIL Open Font License 1.1 (c) LY Corporation.")
    }

    HorizontalDivider()

    Column {
        Row { Text("繁中版说明", style = MaterialTheme.typography.headlineSmall) }
        TextWithLink("本项目原版为 (https://mee1080.github.io/umasim/race/) 翻译主要使用GPT-5.4模型，仅作参考，Logo使用GPT-image2生成")
        TextWithLink("本项目使用「思源黑体」（https://github.com/adobe-fonts/source-han-sans）")
        Text("仅作为学习交流使用")
    }

    HorizontalDivider()

    Column {
        Row { Text("繁中版更新历史", style = MaterialTheme.typography.headlineSmall) }
        Text("2025-06-09：新增马娘搜索框")
        Text("2025-07-13：新增技能发动位置")
        Text("2026-06-11：日常更新，优化马娘搜索框")
    }
}

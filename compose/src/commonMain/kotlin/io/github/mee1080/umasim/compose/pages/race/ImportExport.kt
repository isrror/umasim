package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.MyButton
import io.github.mee1080.umasim.compose.common.atoms.TextWithLink
import io.github.mee1080.umasim.race.calc2.UmaStatus
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.ImportExportConverter
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.importChara

@Composable
fun ImportExport(virtual: Boolean, state: AppState, dispatch: OperationDispatcher<AppState>) {
    val chara by derivedStateOf { state.chara(virtual) }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ImportDialog(virtual, dispatch)
        ExportDialog(chara)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ImportDialog(virtual: Boolean, dispatch: OperationDispatcher<AppState>) {
    var open by remember { mutableStateOf(false) }
    MyButton({ open = true }) {
        Text("导入")
    }
    if (open) {
        var input by remember { mutableStateOf("") }
        var result by remember { mutableStateOf<UmaStatus?>(null) }
        AlertDialog(
            onDismissRequest = { open = false },
            text = {
                Column {
                    Text("※请在框内用 Ctrl+V 等方式粘贴后，点导入（Android 可能不支持）")
                    Text("※格式比较随意，也可以让 ChatGPT 之类先 OCR 再导入")
                    TextWithLink("　例： https://x.com/mee10801/status/1796811671304028542")
                    OutlinedTextField(
                        value = input,
                        onValueChange = { input = it },
                        modifier = Modifier.heightIn(max = 200.dp),
                    )
                    MyButton(
                        onClick = { result = ImportExportConverter.importChara(input) },
                        modifier = Modifier.padding(vertical = 8.dp),
                    ) {
                        Text("导入")
                    }
                    result?.let {
                        Text("角色：${it.charaName}")
                        Text("面板：${it.speed}/${it.stamina}/${it.power}/${it.guts}/${it.wisdom}")
                        Text("适性：场地${it.surfaceFit}/距离${it.distanceFit}/跑法${it.styleFit}")
                        if (it.hasSkills.isNotEmpty()) {
                            Text("技能：")
                            FlowRow(Modifier.padding(start = 16.dp)) {
                                it.hasSkills.forEach { skill ->
                                    Text("${skill.name}, ")
                                }
                            }
                        }
                        Text("※导入内容仅限面板、适性、技能")
                    }
                }
            },
            confirmButton = {
                MyButton(
                    onClick = {
                        result?.let {
                    dispatch(importChara(virtual, it))
                        }
                        open = false
                    },
                    enabled = result != null,
                ) {
                    Text("应用")
                }
            },
            dismissButton = {
                MyButton({ open = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun ExportDialog(chara: UmaStatus) {
    var open by remember { mutableStateOf(false) }
    MyButton({ open = true }) {
        Text("导出")
    }
    if (open) {
        var value by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            value = ImportExportConverter.exportChara(chara)
        }
        AlertDialog(
            onDismissRequest = { open = false },
            text = {
                Column {
                    OutlinedTextField(
                        value = value,
                        onValueChange = {},
                        readOnly = true,
                    )
                    Text("※请在框内用 Ctrl+C 等方式复制（Android 不支持）")
                    Text("※导出内容仅限面板、适性、技能")
                }
            },
            confirmButton = {
                MyButton({ open = false }) {
                    Text("关闭")
                }
            },
        )
    }
}

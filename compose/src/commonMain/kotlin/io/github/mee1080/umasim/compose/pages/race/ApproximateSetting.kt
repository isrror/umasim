package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.race.data.PositionKeepMode
import io.github.mee1080.umasim.race.data.defaultCompeteFightRate
import io.github.mee1080.umasim.race.data.defaultPositionCompetitionRate
import io.github.mee1080.umasim.race.data.defaultSecureLeadRate
import io.github.mee1080.umasim.race.data2.ApproximateMultiCondition
import io.github.mee1080.umasim.race.data2.approximateConditions
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.*
import io.github.mee1080.utility.toPercentString

@Composable
fun ApproximateSetting(state: AppState, dispatch: OperationDispatcher<AppState>) {
    val positionKeepMode by derivedStateOf { state.setting.positionKeepMode }
    val positionKeepRate by derivedStateOf { state.setting.positionKeepRate }
    HorizontalDivider()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val systemSetting = state.systemSetting
        Column {
            Text("近似条件", style = MaterialTheme.typography.headlineSmall)
            Text("以下项目因难以精确模拟，采用了近似处理")
            Text("（之后希望能开放修改）", style = MaterialTheme.typography.bodySmall)
        }

        Column {
            Text("全力冲刺", style = MaterialTheme.typography.titleLarge)
            Text("由于最高速度未知，速度超过 2000 时按无限制处理")
        }

        Column {
            Text("位置保持", style = MaterialTheme.typography.titleLarge)

            SelectBox(
                PositionKeepMode.entries, positionKeepMode,
                onSelect = { dispatch(setPositionKeepMode(it)) },
                modifier = Modifier.width(512.dp),
                label = { Text("模式") },
                itemToString = { it.label },
            )
            when (positionKeepMode) {
                PositionKeepMode.APPROXIMATE -> {
                    Text("在以下区间进入放缓模式")
                    Text("即使处于焦躁状态也会触发（因为位置固定）")
                    Text("逃跑跑法的各模式，以及加速模式，尚未实现")
                    Text(
                        "先行：${
                            systemSetting.positionKeepSectionSen.mapIndexed { index, value -> index to value }
                                .filter { it.second }
                                .joinToString { it.first.toString() }
                        }"
                    )
                    Text(
                        "差：${
                            systemSetting.positionKeepSectionSasi.mapIndexed { index, value -> index to value }
                                .filter { it.second }
                                .joinToString { (it.first + 1).toString() }
                        }"
                    )
                    Text(
                        "追：${
                            systemSetting.positionKeepSectionOi.mapIndexed { index, value -> index to value }
                                .filter { it.second }
                                .joinToString { (it.first + 1).toString() }
                        }"
                    )
                }

                PositionKeepMode.VIRTUAL -> {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Column {
                            Text("按与以下角色的差距判定")
                            Text("不过，逃跑之间的取位争夺尚未实现（虚拟配速马会按一定概率进入加速模式）")
                        }
                        Text("虚拟配速马进入加速模式的概率: $positionKeepRate %")
                        Slider(
                            value = positionKeepRate.toFloat(),
                            onValueChange = { dispatch(setPositionKeepRate(it.toInt())) },
                            valueRange = 0f..100f,
                            steps = 100,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        ImportExport(true, state, dispatch)
                        CharaInput(true, state, dispatch)
                        SkillInput(true, state, dispatch)
                    }
                }

                PositionKeepMode.SPEED_UP -> {
                    Text("会以一定概率进入加速模式（实际还会叠加设置值与智力判定）")
                    Text("概率: $positionKeepRate %")
                    Slider(
                        value = positionKeepRate.toFloat(),
                        onValueChange = { dispatch(setPositionKeepRate(it.toInt())) },
                        valueRange = 0f..100f,
                        steps = 100,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                PositionKeepMode.NONE -> {
                    Text("不进行位置保持判定")
                }
            }
        }

        Column {
            Text("走位", style = MaterialTheme.typography.titleLarge)
            Text("超车模式判定和横向阻挡造成的停移，均采用近似处理（见技能发动部分）")
            Text("处于超车模式时，会在内栏外侧空出一个身位行进")
            Text("当发动目标速度或当前速度类技能时，会按“速度技能发动时走位移动率”的概率向外移动一个身位")
            Text("外绕损耗按所有弯道均为 90 度来计算（以后想做更精确的计算）")
        }

        Column {
            Text("取位争夺", style = MaterialTheme.typography.titleLarge)
            Text("逃跑时会在${systemSetting.leadCompetitionPosition}m的位置固定触发")
        }

        Column {
            Text("追比", style = MaterialTheme.typography.titleLarge)
            Text("在最后直线上每秒按 ${systemSetting.competeFightRate.toPercentString()} 的概率触发")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = (systemSetting.competeFightRate * 100).toFloat(),
                    onValueChange = { dispatch(setCompeteFightRate(it.toDouble() / 100.0)) },
                    valueRange = 0f..100f,
                    steps = 99,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = { dispatch(setCompeteFightRate(defaultCompeteFightRate)) },
                ) { Text("重置") }
            }
        }

        Column {
            Text("脚力十足", style = MaterialTheme.typography.titleLarge)
            Text("持续时间固定为 3 秒×距离系数（0.45/1.0/0.875/0.8）")
            Text("脚力蓄积尚未实现（原本就没解析出来）")
        }

        Column {
            Text("持久力温存", style = MaterialTheme.typography.titleLarge)
            Text("如果体力不足，会按 ${systemSetting.staminaKeepRate.toPercentString()} 的概率触发")
        }

        Column {
            Text("取位调整", style = MaterialTheme.typography.titleLarge)
            Text("与领头的距离判定，以及附近是否有赛马娘的判定，均按始终成功处理")
            Text("若不是持久力温存，则按 ${systemSetting.positionCompetitionRate.toPercentString()} 的概率触发")
            Text("触发次数与实测期望值一致的程度大约只有 40%～60%（在判定耐力是否足够时则按 100% 处理）")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = (systemSetting.positionCompetitionRate * 100).toFloat(),
                    onValueChange = { dispatch(setPositionCompetitionRate(it.toDouble() / 100.0)) },
                    valueRange = 0f..100f,
                    steps = 99,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = { dispatch(setPositionCompetitionRate(defaultPositionCompetitionRate)) },
                ) { Text("重置") }
            }
        }

        Column {
            Text("确保领先", style = MaterialTheme.typography.titleLarge)
            Text("除追跑外，会按 ${systemSetting.secureLeadRate.toPercentString()} 的概率触发")
            Text("当自身跑法为逃跑、位置保持模式为虚拟配速马，且对手跑法与自身不同的时候，速度上升量会附加倍率")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Slider(
                    value = (systemSetting.secureLeadRate * 100).toFloat(),
                    onValueChange = { dispatch(setSecureLeadRate(it.toDouble() / 100.0)) },
                    valueRange = 0f..100f,
                    steps = 99,
                    modifier = Modifier.weight(1f),
                )
                Button(
                    onClick = { dispatch(setSecureLeadRate(defaultSecureLeadRate)) },
                ) { Text("重置") }
            }
        }

        Column {
            Text("耐力对决", style = MaterialTheme.typography.titleLarge)
            Text("虽然随机倍率会落在 0.95～1.02 之间，但这里固定为 1.0 倍")
        }

        Column {
            Text("技能发动", style = MaterialTheme.typography.titleLarge)
            Text("与其他赛马娘相关的技能发动条件，会每秒进行一次以下判定")
            Text(
                "(这里是经验性设定，如果和实战差很多也欢迎提意见)",
                style = MaterialTheme.typography.bodySmall
            )
            approximateConditions.forEach { (_, condition) ->
                Text(condition.displayName, modifier = Modifier.padding(top = 8.dp))
                if (condition.valueOnStart > 0) {
                    Text("开局时判定开启", modifier = Modifier.padding(start = 8.dp))
                }
                if (condition is ApproximateMultiCondition) {
                    condition.conditions.forEach {
                        Text(
                            "${it.first.displayName} : ${it.first.description}",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                } else {
                    Text(condition.description, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}

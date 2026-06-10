package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import io.github.mee1080.umasim.race.data.Style
import io.github.mee1080.umasim.race.data.secondPerFrame
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.utility.roundToString
import kotlin.math.roundToInt

@Composable
fun LastSimulationDetailOutput(state: AppState) {
    val settingState = derivedStateOf { state.lastSimulationSettingWithPassive }
    val setting = settingState.value ?: return
    Column {
        Text("修正后面板：${setting.modifiedSpeed}/${setting.modifiedStamina}/${setting.modifiedPower}/${setting.modifiedGuts}/${setting.modifiedWisdom}")
        Text(
            "初始耐力：${setting.spMax.roundToString(2)}/金回复≈${
                setting.equalStamina(550).roundToInt()
            }耐力/白回复≈${
                setting.equalStamina(150).roundToInt()
            }耐力/终盘耐力消耗系数：${
                setting.spurtSpCoef.roundToString(3)
            }"
        )
        Text(
            "技能发动率：${setting.skillActivateRate.roundToString(1)}%/焦躁率：${
                setting.temptationRate.roundToString(1)
            }%"
        )
        Text("起步　目标速度：${setting.v0.roundToString(2)} 加速度：${setting.a0.roundToString(2)}")
        Text("前期　目标速度：${setting.v1.roundToString(2)} 加速度：${setting.a1.roundToString(2)}")
        Text("中期　目标速度：${setting.v2.roundToString(2)} 加速度：${setting.a2.roundToString(2)}")
        Text("后期　目标速度：${setting.v3.roundToString(2)} 加速度：${setting.a3.roundToString(2)}")
        Text("最高冲刺速度：${setting.maxSpurtSpeed.roundToString(2)}")
        if (setting.runningStyle == Style.NIGE) {
            Text("取位争夺　速度：${setting.leadCompetitionSpeed.roundToString(2)}")
        }
        Text(
            "取位调整　速度：${setting.positionCompetitionSpeed.roundToString(2)} 耐力：${
                setting.positionCompetitionStamina.roundToString(2)
            }"
        )
        if (setting.runningStyle != Style.OI) {
            Text(
                "确保领先　速度：${setting.secureLeadSpeed.roundToString(2)} 耐力：${
                    setting.secureLeadStamina.roundToString(2)
                }"
            )
        }
        Text(
            "脚力十足　加速度基准值：${setting.conservePowerAccelerationBase.roundToString(2)} 持续时间：${
                (setting.conservePowerFrame * secondPerFrame).roundToString(2)
            }"
        )
        Text("耐力对决　速度：${setting.staminaLimitBreakSpeed.roundToString(2)}")
        Text(
            "追比　速度：${setting.competeFightSpeed.roundToString(2)} 加速度：${
                setting.competeFightAcceleration.roundToString(2)
            }"
        )
        Text(
            "智力技能强化　前期：${
                setting.wisdomSkillBuff[0]!!.roundToString(2)
            } 中期：${
                setting.wisdomSkillBuff[1]!!.roundToString(2)
            } 后期：${
                setting.wisdomSkillBuff[2]!!.roundToString(2)
            } 最终段：${
                setting.wisdomSkillBuff[3]!!.roundToString(2)
            }"
        )
    }
}

package io.github.mee1080.umasim.compose.pages.race

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import io.github.mee1080.umasim.compose.common.atoms.LabeledCheckbox
import io.github.mee1080.umasim.compose.common.atoms.SelectBox
import io.github.mee1080.umasim.store.AppState
import io.github.mee1080.umasim.store.SimulationMode
import io.github.mee1080.umasim.store.framework.OperationDispatcher
import io.github.mee1080.umasim.store.operation.setContributionTarget
import io.github.mee1080.umasim.store.operation.setSimulationMode

@Composable
fun ModeInput(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SelectBox(
            SimulationMode.entries, state.simulationMode,
            onSelect = { dispatch(setSimulationMode(it)) },
            label = { Text("模式") },
            itemToString = { it.label },
        )
        if (state.simulationMode == SimulationMode.CONTRIBUTION || state.simulationMode == SimulationMode.CONTRIBUTION2) {
            ContributionSetting(state, dispatch)
        }
    }
}

@Composable
private fun ContributionSetting(state: AppState, dispatch: OperationDispatcher<AppState>) {
    Column {
        val statusValue = if (state.simulationMode == SimulationMode.CONTRIBUTION) -100 else 100
        listOf(
            "速度" to "速度",
            "耐力" to "耐力",
            "力量" to "力量",
            "根性" to "根性",
            "智力" to "智力",
        ).forEach { (idName, displayName) ->
            val id = "/status_${idName}_$statusValue"
            val text = if (statusValue >= 0) "$displayName+$statusValue" else "$displayName$statusValue"
            ContributionSettingEntry(state.contributionTargets, dispatch, id, text)
        }
        if (state.simulationMode == SimulationMode.CONTRIBUTION) {
            state.setting.umaStatus.surfaceFit.down()?.let {
                val from = state.setting.umaStatus.surfaceFit
                val id = "/fit_场地_${from.ordinal}_${it.ordinal}"
                val text = "场地 ${from.name}->${it.name}"
                ContributionSettingEntry(state.contributionTargets, dispatch, id, text)
            }
            state.setting.umaStatus.distanceFit.down()?.let {
                val from = state.setting.umaStatus.distanceFit
                val id = "/fit_距离_${from.ordinal}_${it.ordinal}"
                val text = "距离 ${from.name}->${it.name}"
                ContributionSettingEntry(state.contributionTargets, dispatch, id, text)
            }
            state.setting.umaStatus.styleFit.down()?.let {
                val from = state.setting.umaStatus.styleFit
                val id = "/fit_跑法_${from.ordinal}_${it.ordinal}"
                val text = "跑法 ${from.name}->${it.name}"
                ContributionSettingEntry(state.contributionTargets, dispatch, id, text)
            }
        } else {
            state.setting.umaStatus.surfaceFit.up()?.let {
                val from = state.setting.umaStatus.surfaceFit
                val id = "/fit_场地_${from.ordinal}_${it.ordinal}"
                val text = "场地 ${from.name}->${it.name}"
                ContributionSettingEntry(state.contributionTargets, dispatch, id, text)
            }
            state.setting.umaStatus.distanceFit.up()?.let {
                val from = state.setting.umaStatus.distanceFit
                val id = "/fit_距离_${from.ordinal}_${it.ordinal}"
                val text = "距离 ${from.name}->${it.name}"
                ContributionSettingEntry(state.contributionTargets, dispatch, id, text)
            }
            state.setting.umaStatus.styleFit.up()?.let {
                val from = state.setting.umaStatus.styleFit
                val id = "/fit_跑法_${from.ordinal}_${it.ordinal}"
                val text = "跑法 ${from.name}->${it.name}"
                ContributionSettingEntry(state.contributionTargets, dispatch, id, text)
            }
        }
        state.hasSkills(false).forEach { skill ->
            ContributionSettingEntry(state.contributionTargets, dispatch, skill.id, skill.name)
        }
    }
}

@Composable
fun ContributionSettingEntry(
    contributionTargets: Set<String>,
    dispatch: OperationDispatcher<AppState>,
    id: String,
    text: String,
) {
    LabeledCheckbox(
        selected = contributionTargets.contains(id),
        onCheckedChange = { dispatch(setContributionTarget(id, it)) },
    ) {
        Text(text)
    }
}

package cicada.client.feature.module.modules.world.autobridge.pitchsort

import cicada.client.setting.value.ChoiceValue

// SCWGxD regrets everything he did. 01.05.2026 13:07.
abstract class PitchesSortMode(name: String) : ChoiceValue.Choice(name) {
    abstract fun sort(pitches: List<Float>): List<Float>
}
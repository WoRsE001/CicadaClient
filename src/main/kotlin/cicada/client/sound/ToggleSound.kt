package cicada.client.sound

import cicada.client.CicadaClient
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent

// SCWGxD regrets everything he did. 01.05.2026 9:41.
class ToggleSound(val name: String) {
    val enableSound = SoundEvent.createVariableRangeEvent(CicadaClient.of("sounds/toggle/${name.lowercase()}_enable"))
    val disableSound = SoundEvent.createVariableRangeEvent(CicadaClient.of("sounds/toggle/${name.lowercase()}_disable"))

    init {
        Registry.register(BuiltInRegistries.SOUND_EVENT, enableSound.location, enableSound)
        Registry.register(BuiltInRegistries.SOUND_EVENT, disableSound.location, disableSound)
    }

    fun sound(state: Boolean) = if (state) enableSound else disableSound
}
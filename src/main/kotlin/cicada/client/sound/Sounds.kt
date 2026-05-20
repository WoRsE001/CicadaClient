package cicada.client.sound

import cicada.client.utils.client.mc
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundEvent

// SCWGxD regrets everything he did. 01.05.2026 9:25.
val INTUITION_TOGGLE = ToggleSound("Intuition")
val NOTIFY_TOGGLE = ToggleSound("Notify")
val SIGMA_TOGGLE = ToggleSound("Sigma")

fun play(sound: SoundEvent, pitch: Float) =
     mc.soundManager.play(SimpleSoundInstance.forUI(sound, pitch))

fun play(sound: SoundEvent) = play(sound, 1f)

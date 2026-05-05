package cicada.client.utils

import com.mojang.blaze3d.platform.Window
import com.mojang.blaze3d.systems.GpuDevice
import com.mojang.blaze3d.systems.RenderSystem
import cicada.client.CicadaClient
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component

val Window.dimensions
    get() = intArrayOf(width, height)

val Window.scaledDimension
    get() = intArrayOf(guiScaledWidth, guiScaledHeight)

val mc: Minecraft
    inline get() = Minecraft.getInstance()
val player: LocalPlayer
    inline get() = mc.player!!
val level: ClientLevel
    inline get() = mc.level!!
val connection: ClientPacketListener
    inline get() = mc.connection!!
val gameMode: MultiPlayerGameMode
    inline get() = mc.gameMode!!
val gpuDevice: GpuDevice
    inline get() = RenderSystem.getDevice()

// сделал брат <3
fun Minecraft.displayMessage(content: Any?) =
    gui.chat.addClientSystemMessage(Component.literal("[${CicadaClient.NAME}] ${content.toString()}"))

private var timer_ = 1f

var Minecraft.timer: Float
    get() = timer_
    set(value) { timer_ = value }

fun nullCheck() = mc.player != null && mc.level != null

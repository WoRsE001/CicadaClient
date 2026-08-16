package cicada.client.utils.client

import cicada.client.CicadaClient
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.MultiPlayerGameMode
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component

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

fun Minecraft.displayMessage(content: Any?) =
    gui.chat.addClientSystemMessage(Component.literal("[${CicadaClient.NAME}] ${content.toString()}"))

private var _gameSpeed = 1f

var Minecraft.gameSpeed: Float
    get() = _gameSpeed
    set(value) { _gameSpeed = value }

fun nullCheck() = mc.player != null && mc.level != null

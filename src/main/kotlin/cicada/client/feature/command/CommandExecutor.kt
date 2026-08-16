package cicada.client.feature.command

import cicada.client.event.Event
import cicada.client.event.EventListener
import cicada.client.event.events.EventChatMessage
import cicada.client.utils.client.displayMessage
import cicada.client.utils.client.mc
import cicada.client.utils.client.nullCheck
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component

object CommandExecutor : EventListener {
    init {
        registerToEvents()
    }

    override fun onEvent(event: Event) {
        if (event is EventChatMessage.Send) {
            val prefix = CommandManager.prefix

            if (!event.content.startsWith(prefix)) return

            val commandText = event.content.removePrefix(prefix)

            try {
                CommandManager.execute(commandText)
            } catch (e: CommandException) {
                mc.displayMessage(e.component.copy().withStyle(ChatFormatting.RED))

                if (e.usageInfo.isNotEmpty()) {
                    mc.displayMessage(Component.literal("Использование:").withStyle(ChatFormatting.YELLOW))
                    for (usage in e.usageInfo) {
                        val text = Component.literal("⬥ $prefix")
                            .append(usage)
                            .withStyle { it.withClickEvent(ClickEvent.SuggestCommand(prefix + usage.string)) }
                        mc.displayMessage(text)
                    }
                }
            } catch (e: Exception) {
                mc.displayMessage(Component.literal("Произошла ошибка: ${e.message ?: "неизвестно"}"))
            } finally {
                event.cancel()
            }
        }
    }

    override fun shouldListenEvents() = nullCheck()
}

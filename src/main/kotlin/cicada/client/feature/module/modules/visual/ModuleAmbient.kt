package cicada.client.feature.module.modules.visual

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory

// SCWGxD regrets everything he did. 22.05.2026 10:16.
object ModuleAmbient : ClientModule("Ambient", ModuleCategory.VISUAL, description = "BeautifiesTheWorld") {
    private val time = toggleableGroup("Time", false)
        private val worldTime by time.float("WorldTime", 1f, -1f..1f)
        //private val dynamicWorldTime by time.boolean("Dynamic world time", false)
        //private val worldTimeSpeed by time.float("World time speed", 0f, 0f..2f, "days per sec").visible { dynamicWorldTime }
    val weather = toggleableGroup("Weather", false)
        val rain by time.float("Rain", 0f, 0f..1f)
        val thunder by time.float("Thunder", 0f, 0f..1f)
        val snow by boolean("Snow", false)

    fun getTime(original: Long): Long {
        if (time.toggled) {
            return (worldTime * 17000).toLong() + 1000
        }

        return original
    }
}
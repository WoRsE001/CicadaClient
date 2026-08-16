package cicada.client.feature.hud.huds.modulelist.fakemodules

import cicada.client.feature.module.ClientModule
import cicada.client.feature.module.ModuleCategory
import cicada.client.feature.module.Modules

object ModuleTest : ClientModule("Test", ModuleCategory.COMBAT) {
    init {
        Modules -= this
    }
}
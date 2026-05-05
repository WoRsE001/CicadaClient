package cicada.client.setting.preset

import cicada.client.setting.Configureable
import cicada.client.utils.player
import cicada.client.utils.target.SortType
import cicada.client.utils.target.bestTargetBy
import cicada.client.utils.target.validTargets
import net.minecraft.world.entity.LivingEntity

// Blood! It's everywhere. SCWxD killed you on 08.03.2026 at 9:24.
open class TargetFinder : Configureable("Finding target") {
    val searchRange by float("Search range", 6f, 3f..20f, "%.1")
    val sortType = choice("Sort type").apply {
        choice("FOV").select()
        choice("Distance")
        choice("Health")
        choice("HurtTime")
    }
    val lockTarget = toggleableGroup("Lock target", false)
    val lockTargetRange by lockTarget.float("Range", 6f, 3f..20f, "%.1")

    var target: LivingEntity? = null

    fun updateTarget() {
        if (lockTarget.toggled)
            if (target != null && !target!!.isDeadOrDying && player.distanceTo(target!!) <= lockTargetRange)
                return


        target = validTargets(searchRange).bestTargetBy(SortType.valueOf(sortType.inner?.name ?: "FOV"))
    }

    fun resetTarget() {
        target = null
    }
}
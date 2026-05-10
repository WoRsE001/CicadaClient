package cicada.client.setting.preset

import cicada.client.setting.Configureable
import cicada.client.utils.level
import cicada.client.utils.player
import cicada.client.utils.player.isTeam
import cicada.client.utils.target.BestEntityBy
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player

// Blood! It's everywhere. SCWxD killed you on 08.03.2026 at 9:24.
open class TargetFinder : Configureable("Finding target") {
    val searchRange by float("Search range", 6f, 3f..20f, "%.1")
    val filter = multiChoice("Filter")
    val filterAnimals = filter.choice("Animals", true)
    val filterInvisible = filter.choice("Invisible")
    val filterFriends = filter.choice("Friends", true)
    val filterMonsters = filter.choice("Monsters", true)
    val filterPlayers = filter.choice("Players")
    val filterTeams = filter.choice("Teams", true)
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

        val validTargets = mutableListOf<LivingEntity>()

        for (entity in level.entitiesForRendering()) {
            if (entity !is LivingEntity || entity == player) continue
            if (entity.isDeadOrDying || player.distanceTo(entity) > searchRange) continue
            if (filterAnimals.toggled && entity is Animal) continue
            if (filterMonsters.toggled && entity is Monster) continue
            if (filterInvisible.toggled && entity.hasEffect(MobEffects.INVISIBILITY)) continue
            if (entity is Player) {
                if (filterPlayers.toggled) continue
                if (filterTeams.toggled && entity.isTeam) continue
            }

            validTargets += entity
        }

        target = BestEntityBy.valueOf(sortType.inner?.name ?: sortType.choices.first().name).entity(validTargets)
    }

    fun resetTarget() {
        target = null
    }
}
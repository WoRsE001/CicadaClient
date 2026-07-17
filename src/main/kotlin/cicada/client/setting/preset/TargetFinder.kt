package cicada.client.setting.preset

import cicada.client.setting.value.Configurable
import cicada.client.utils.client.level
import cicada.client.utils.client.player
import cicada.client.utils.player.isTeam
import cicada.client.utils.target.BestEntityBy
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.monster.Monster
import net.minecraft.world.entity.player.Player

// Blood! It's everywhere. SCWxD killed you on 08.03.2026 at 9:24.
open class TargetFinder : Configurable("Target finder") {
    var searchRange = float("Search range", 10f, 0f..20f)
    var filter = multiChoice("Filter")
    var filterAnimals = filter.choice("Animals", true)
    var filterInvisible = filter.choice("Invisible", false)
    var filterFriends = filter.choice("Friends", true)
    var filterMonsters = filter.choice("Monsters", true)
    var filterPlayers = filter.choice("Players", false)
    var filterTeams = filter.choice("Teams", true)
    var sortType = choice("Sort type").apply {
        choice("FOV")
        choice("Distance").select()
        choice("Health")
        choice("HurtTime")
    }
    var lockTarget = toggleableGroup("Lock target", false)
    var lockTargetRange = lockTarget.float("Range", 6f, 3f..20f, "%.1")

    var target: LivingEntity? = null

    fun updateTarget() {
        filterMonsters.toggled = false
        if (lockTarget.toggled)
            if (target != null && !target!!.isDeadOrDying && player.distanceTo(target!!) <= lockTargetRange.inner)
                return

        val validTargets = mutableListOf<LivingEntity>()

        for (entity in level.entitiesForRendering()) {
            if (entity !is LivingEntity || entity == player) continue
            if (entity.isDeadOrDying || player.distanceTo(entity) > searchRange.inner) continue
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
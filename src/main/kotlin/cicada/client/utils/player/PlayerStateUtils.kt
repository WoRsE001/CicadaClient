package cicada.client.utils.player

import cicada.client.feature.module.modules.misc.ModuleMurderMysteryHelper
import cicada.client.feature.module.modules.misc.ModuleTeams
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.player.Input
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

val Player.isMurder: Boolean
    get() = this in ModuleMurderMysteryHelper.murders

val Player.isDetective: Boolean
    get() = this in ModuleMurderMysteryHelper.detectives

val Player.isTeam: Boolean
    get() = this in ModuleTeams.teams

val Player.isFriend: Boolean
    get() = this.name.string in Friends

var LocalPlayer.velocityX: Double
    get() = deltaMovement.x
    set(value) {
        deltaMovement = Vec3(value, deltaMovement.y, deltaMovement.z)
    }

var LocalPlayer.velocityY: Double
    get() = deltaMovement.y
    set(value) {
        deltaMovement = Vec3(deltaMovement.x, value, deltaMovement.z)
    }

var LocalPlayer.velocityZ: Double
    get() = deltaMovement.z
    set(value) {
        deltaMovement = Vec3(deltaMovement.x, deltaMovement.y, value)
    }

val Input.isMoving: Boolean
    get() = forward != backward || left != right

var utilGroundTick = 0
var utilAirTick = 0

val LocalPlayer.groundTick: Int
    get() = utilGroundTick

val LocalPlayer.airTick: Int
    get() = utilGroundTick

fun Player.canCrit() = fallDistance > 0f && !onGround() && !onClimbable() && !isInWater && !isMobilityRestricted && !isPassenger
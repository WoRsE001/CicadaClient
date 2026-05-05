package cicada.client.utils.player

import cicada.client.module.impl.misc.ModuleMurderMysteryHelper
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Input
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3

val Player.isMurder: Boolean
    get() = this in ModuleMurderMysteryHelper.murders

val Player.isDetective: Boolean
    get() = this in ModuleMurderMysteryHelper.detectives

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

val Entity.lastPos: Vec3
    get() = Vec3(xo, yo, zo)

val Input.isMoving: Boolean
    get() = forward != backward || left != right

var utilAirTicks = 0
var utilGroundTicks = 0

val LocalPlayer.airTicks: Int
    get() = utilAirTicks

val LocalPlayer.groundTicks: Int
    get() = utilGroundTicks

fun Player.canCrit() = fallDistance > 0f && !onGround() && !onClimbable() && !isInWater && !isMobilityRestricted && !isPassenger
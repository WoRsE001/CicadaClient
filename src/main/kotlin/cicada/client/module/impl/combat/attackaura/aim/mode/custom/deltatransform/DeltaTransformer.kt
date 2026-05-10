package cicada.client.module.impl.combat.attackaura.aim.mode.custom.deltatransform

import cicada.client.setting.ToggleableConfigureable
import cicada.client.utils.rotation.Rotation

abstract class DeltaTransformer(name: String) : ToggleableConfigureable(name, false) {
    abstract fun transform(delta: Rotation): Rotation
}
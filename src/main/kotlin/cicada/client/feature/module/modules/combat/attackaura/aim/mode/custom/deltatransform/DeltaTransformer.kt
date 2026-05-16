package cicada.client.feature.module.modules.combat.attackaura.aim.mode.custom.deltatransform

import cicada.client.config.types.ToggleableConfigurable
import cicada.client.utils.rotation.Rotation

abstract class DeltaTransformer(name: String) : ToggleableConfigurable(name, false) {
    abstract fun transform(delta: Rotation): Rotation
}
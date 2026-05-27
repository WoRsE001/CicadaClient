package cicada.client.rotation.ai

import cicada.client.CicadaClient
import cicada.client.utils.rotation.Rotation
import net.minecraft.world.phys.Vec3
import java.io.File

// SCWGxD regrets everything he did. 27.05.2026 7:43.
data class Data(val diff: Vec3, val rotation: Rotation, val bestRotation: Rotation)

val dataSetFolder = File(CicadaClient.rootFolder, "data-sets")
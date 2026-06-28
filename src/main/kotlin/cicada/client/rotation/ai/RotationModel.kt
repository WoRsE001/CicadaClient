package cicada.client.rotation.ai

/**
 * Holds the currently selected rotation model, loaded via `model use <name>`.
 * Consumers (e.g. an aim mode) read [active] and call [RotationNN.predict] with a
 * `history * SAMPLE_SIZE - 2` input vector built the same way as in [RotationNN.parseDataSet].
 */
object RotationModel {
    @Volatile
    var active: RotationNN? = null
}
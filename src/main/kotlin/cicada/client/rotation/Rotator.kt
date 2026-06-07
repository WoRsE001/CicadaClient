package cicada.client.rotation

interface Rotator {
    val rotatePriority: Int

    fun rotate()
    fun willRotate(): Boolean

    fun registerToRotations() {
        RotationHandler += this
    }
}
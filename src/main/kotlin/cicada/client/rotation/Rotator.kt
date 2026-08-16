package cicada.client.rotation

interface Rotator {
    val rotatePriority: Int

    fun registerToRotations() {
        RotationHandler += this
    }

    fun willRotate(): Boolean
    fun rotate()
}
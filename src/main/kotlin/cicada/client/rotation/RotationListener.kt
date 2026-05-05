package cicada.client.rotation

// мб переимонавть?
interface RotationListener {
    val rotatePriority: Int

    fun rotate()
    fun willRotate(): Boolean

    fun registerToRotations() {
        RotationHandler += this
    }
}
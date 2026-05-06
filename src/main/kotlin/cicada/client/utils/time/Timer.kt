package cicada.client.utils.time

class Timer {
    var lastTime = System.currentTimeMillis()

    fun reached(time: Int) = System.currentTimeMillis() - lastTime >= time

    fun reached(time: Int, runnable: Runnable) {
        if (reached(time)) {
            runnable.run()
            reset()
        }
    }

    fun reached(time: Float) = System.currentTimeMillis() - lastTime >= time

    fun reached(time: Float, runnable: Runnable) {
        if (reached(time)) {
            runnable.run()
            reset()
        }
    }

    fun reset() {
        lastTime = System.currentTimeMillis()
    }
}
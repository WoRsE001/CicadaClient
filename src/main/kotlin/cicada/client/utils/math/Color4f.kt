package cicada.client.utils.math

// SCWGxD regrets everything he did. 30.03.2026 5:32.
class Color4f(var r: Float, var g: Float, var b: Float, var a: Float) {
    var hue: Float
        get() {
            val max = maxOf(r, g, b)
            val min = minOf(r, g, b)
            val delta = max - min
            if (delta == 0f) return 0f
            val hue = when (max) {
                r -> 60f * ((g - b) / delta % 6f)
                g -> 60f * ((b - r) / delta + 2f)
                else -> 60f * ((r - g) / delta + 4f)
            }
            return (if (hue < 0) hue + 360f else hue) / 360f
        }
        set(value) = setFromHSB(value, saturation, brightness)

    var saturation: Float
        get() {
            val max = maxOf(r, g, b)
            val min = minOf(r, g, b)
            val delta = max - min
            return if (max == 0f) 0f else delta / max
        }
        set(value) = setFromHSB(hue, value, brightness)

    var brightness: Float
        get() = maxOf(r, g, b)
        set(value) = setFromHSB(hue, saturation, value)

    constructor(r: Float, g: Float, b: Float) : this(r, g, b, 1f)
    constructor(b: Float, a: Float) : this(b, b, b, a)
    constructor(b: Float) : this(b, 1f)
    constructor(color: Int) : this(
        (color shr 16 and 0xFF) / 255f,
        (color shr 8 and 0xFF) / 255f,
        (color and 0xFF) / 255f,
        (color ushr 24) / 255f
    )

    fun toInt() =
        ((a * 255).toInt() and 0xFF) shl 24 or
                (((r * 255).toInt() and 0xFF) shl 16) or
                (((g * 255).toInt() and 0xFF) shl 8) or
                ((b * 255).toInt() and 0xFF)

    fun setFromARGB(color: Int) {
        r = (color shr 16 and 0xFF) / 255f
        g = (color shr 8 and 0xFF) / 255f
        b = (color and 0xFF) / 255f
        a = (color ushr 24) / 255f
    }

    private fun setFromHSB(hue: Float, sat: Float, bri: Float) {
        if (sat == 0f) {
            r = bri
            g = bri
            b = bri
            return
        }
        val h = hue * 360f
        val sector = (h / 60f).toInt()
        val f = h / 60f - sector
        val p = bri * (1f - sat)
        val q = bri * (1f - sat * f)
        val t = bri * (1f - sat * (1f - f))
        when (sector) {
            0 -> {
                r = bri
                g = t
                b = p
            }
            1 -> {
                r = q
                g = bri
                b = p
            }
            2 -> {
                r = p
                g = bri
                b = t
            }
            3 -> {
                r = p
                g = q
                b = bri
            }
            4 -> {
                r = t
                g = p
                b = bri
            }
            else -> {
                r = bri
                g = p
                b = q
            }
        }
    }
}
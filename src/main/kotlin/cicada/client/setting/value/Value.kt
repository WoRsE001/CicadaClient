package cicada.client.setting.value

import cicada.client.config.AsJson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Value<T>(val name: String, private val default: T, val description: String = "") : ReadWriteProperty<Any?, T>,
    AsJson {
    var inner = default

    var visible = { true }
        private set

    protected open fun get() = inner

    protected open fun set(value: T) {
        inner = value
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>) =
        get()

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        set(value)
    }

    open fun resetToDefault() {
        inner = default
    }

    fun visible(visible: () -> Boolean) = apply {
        this.visible = visible
    }

    override fun toString() = name
}
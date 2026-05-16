package cicada.client.config.types

import kotlinx.serialization.json.JsonObject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Value<T>(val name: String, private val default: T) : ReadWriteProperty<Any?, T> {
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

    abstract fun serializeTo(): JsonObject

    abstract fun deserializeFrom(jsonObject: JsonObject)


    override fun toString() = name
}
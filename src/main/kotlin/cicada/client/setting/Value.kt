package cicada.client.setting

import cicada.client.utils.render.RenderableObject
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Value<T>(val name: String, private val default: T) : SaveLoadable, ReadWriteProperty<Any?, T>,
    RenderableObject {
    var inner = default

    var visible = { true }
        private set

    var description: String = ""
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

    fun description(description: String) = apply {
        this.description = description
    }

    override fun toString() = name
}
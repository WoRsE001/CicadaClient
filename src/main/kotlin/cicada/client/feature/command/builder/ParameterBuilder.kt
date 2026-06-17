package net.ccbluex.liquidbounce.features.command.builder

import cicada.client.feature.command.Parameter
import cicada.client.feature.module.ModuleManager

class ParameterBuilder<T : Any>(private val name: String) {

    private var verifier: Parameter.Verifier? = null
    private var required: Boolean = true
    private var vararg: Boolean = false

    companion object {
        val STRING_VALIDATOR = Parameter.Verifier { input ->
            Parameter.Verifier.Ok(input)
        }

        val INTEGER_VALIDATOR = Parameter.Verifier { input ->
            val num = input.toIntOrNull()
            if (num != null) Parameter.Verifier.Ok(num)
            else Parameter.Verifier.Error("'$input' — не число")
        }

        val POSITIVE_INTEGER_VALIDATOR = Parameter.Verifier { input ->
            val num = input.toIntOrNull()
            when {
                num == null -> Parameter.Verifier.Error("'$input' — не число")
                num >= 0 -> Parameter.Verifier.Ok(num)
                else -> Parameter.Verifier.Error("Число должно быть положительным")
            }
        }

        val BOOLEAN_VALIDATOR = Parameter.Verifier { input ->
            when (input.lowercase()) {
                "yes", "on", "true" -> Parameter.Verifier.Ok(true)
                "no", "off", "false" -> Parameter.Verifier.Ok(false)
                else -> Parameter.Verifier.Error("'$input' — не булево значение (ожидается: yes/no, on/off, true/false)")
            }
        }

        val MODULE_VALIDATOR = Parameter.Verifier { input ->
            val module = ModuleManager.find { it.name.equals(input, ignoreCase = true) }
            if (module != null) Parameter.Verifier.Ok(module)
            else Parameter.Verifier.Error("Модуль '$input' не найден")
        }
    }

    fun verifiedBy(verifier: Parameter.Verifier) = apply {
        this.verifier = verifier
    }

    fun required() = apply { required = true }
    fun optional() = apply { required = false }

    fun vararg() = apply { vararg = true }

    fun build() = Parameter(name, required, vararg, verifier)
}

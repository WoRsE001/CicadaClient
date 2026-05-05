package cicada.client.utils.input

import com.mojang.blaze3d.platform.InputConstants

// SCWGxD regrets everything he did. 10.04.2026 7:43.
fun inputByName(name: String): InputConstants.Key {
    if (name.equals("NONE", true)) {
        return InputConstants.UNKNOWN
    }

    val formattedName = name.replace('_', '.')
    val translationKey =
        when {
            formattedName.startsWith("key.mouse.", ignoreCase = true) ||
                    formattedName.startsWith("key.keyboard.", ignoreCase = true) -> formattedName.lowercase()

            formattedName.startsWith("mouse.", ignoreCase = true) ||
                    formattedName.startsWith("keyboard.", ignoreCase = true) -> "key.$formattedName"

            else -> "key.keyboard.${formattedName.lowercase()}"
        }
    return InputConstants.getKey(translationKey)
}
package cicada.client.utils.file

import java.io.File

// SCWGxD regrets everything he did. 27.05.2026 7:59.
fun getNextFile(directory: File, name: String, extension: String): File {
    var index = 0

    while (true) {
        val file = File(directory, name + extension)

        if (!file.exists()) {
            return file
        }

        index++
    }
}

fun createNextFile(directory: File, name: String, extension: String): File {
    val file = getNextFile(directory, name, extension)
    file.createNewFile()
    return file
}
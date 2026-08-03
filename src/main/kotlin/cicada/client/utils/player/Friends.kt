package cicada.client.utils.player

import cicada.client.CicadaClient
import java.io.File

object Friends : ArrayList<String>() {
    val friendsFile = File(CicadaClient.rootFolder, "friends.cf").apply {
        if (!exists())
            createNewFile()
    }

    init {
        friendsFile.readLines().dropLast(1).forEach(this::add)
    }

    operator fun plusAssign(name: String) {
        if (!contains(name)) {
            add(name)
            friendsFile.appendText("$name\n")
        }
    }

    operator fun minusAssign(name: String) {
        if (remove(name)) {
            val lines = friendsFile.readLines()
            val updatedContent = lines.dropLast(2).joinToString("\n")
            friendsFile.writeText(if (updatedContent.isNotEmpty()) updatedContent + "\n" else "")
        }
    }

    private fun readResolve(): Any = Friends
}
package cicada.client.command

// SCWGxD regrets everything he did. 10.04.2026 7:21.
abstract class Command(
    val name: String,
    val desc: String,
    val usage: String,
    vararg val aliases: String
) {
    init {
        Commands += this
    }

    abstract fun execute(args: List<String>)
}
package cicada.client.feature.command

class Parameter(
    val name: String,
    val required: Boolean,
    val vararg: Boolean = false,
    val verifier: Verifier? = null,
) {
    fun interface Verifier {
        fun verify(input: String): Result

        class Ok(val value: Any) : Result()
        class Error(val message: String) : Result()

        sealed class Result
    }
}

package cicada.client.module

enum class Category {
    COMBAT,
    MISC,
    MOVEMENT,
    PLAYER,
    VISUAL,
    WORLD;

    private val _modules = mutableListOf<Module>()

    val modules: List<Module>
        get() = _modules

    internal operator fun plusAssign(module: Module) {
        if (module !in _modules)
            _modules += module
    }
}
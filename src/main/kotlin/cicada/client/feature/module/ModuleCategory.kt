package cicada.client.feature.module

enum class ModuleCategory {
    COMBAT,
    MISC,
    MOVEMENT,
    PLAYER,
    VISUAL,
    WORLD;

    private val _modules = mutableListOf<ClientModule>()

    val modules: List<ClientModule>
        get() = _modules

    internal operator fun plusAssign(module: ClientModule) {
        if (module !in _modules)
            _modules += module
    }
}
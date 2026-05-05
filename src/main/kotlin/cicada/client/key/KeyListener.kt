package cicada.client.key

// created by dicves_recode on 22.02.2026
interface KeyListener {
    var keybind: Keybind

    fun onKey(action: Int)
    fun listenKeybinds(): Boolean

    fun registerToKeybinds() {
        KeyCaller += this
    }
}

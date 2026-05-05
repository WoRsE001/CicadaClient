package cicada.client.setting

// Blood! It's everywhere. SCWxD killed you on 30.03.2026 at 3:03.
interface Toggleable {
    var toggled: Boolean

    fun toggle() {
        toggled = !toggled
    }

    fun onEnable() {}
    fun onDisable() {}
}
package cicada.client.packethandle

// SCWGxD regrets everything he did. 16.07.2026 13:59.
interface PacketDelayer {
    fun getDelay(): Int
    fun shouldDetain(): Boolean

    fun handleWithDelay(delay: Int) {
        PacketHandler.handleWithDelay(this, 0)
    }
}
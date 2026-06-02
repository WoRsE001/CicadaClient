package cicada.client.connection

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

// SCWGxD regrets everything he did. 02.06.2026 16:37.
object Client {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null
    private val isRunning = AtomicBoolean(false)

    var onMessage: ((message: String) -> Unit)? = null
    var onConnected: ((ip: String, port: Int) -> Unit)? = null
    var onDisconnected: (() -> Unit)? = null

    fun


            connect(ip: String, port: Int) {
        if (isRunning.get()) {
            error("Client already connected")
        }

        socket = Socket(ip, port)
        writer = PrintWriter(socket!!.getOutputStream(), true)
        isRunning.set(true)

        onConnected?.invoke(ip, port)

        thread {
            listenForMessages(socket!!)
        }
    }

    fun sendMessage(message: String) {
        if (!isRunning.get() || writer == null) {
            error("Client is not running")
        }

        writer?.println(message)
    }

    fun disconnect() {
        if (!isRunning.get()) return

        isRunning.set(false)
        try { socket?.close() } catch (_: Exception) { }
        socket = null
        writer = null
        onDisconnected?.invoke()
    }

    private fun listenForMessages(currentSocket: Socket) {
        try {
            val input = BufferedReader(InputStreamReader(currentSocket.getInputStream()))
            var message: String? = null
            while (isRunning.get() && input.readLine().also { message = it } != null) {
                message?.let { onMessage?.invoke(it) }
            }
        } catch (e: Exception) {
            // Ошибка чтения (сервер разорвал соединение или сокет закрыт локально)
        } finally {
            disconnect()
        }
    }
}
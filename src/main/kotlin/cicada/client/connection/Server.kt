package cicada.client.connection

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.set
import kotlin.concurrent.thread

// SCWGxD regrets everything he did. 02.06.2026 16:04.
object Server {
    private val clients = ConcurrentHashMap<Int, ClientConnection>()
    private val idCounter = AtomicInteger(1)

    private var serverSocket: ServerSocket? = null
    private val isRunning = AtomicBoolean(false)

    var onMessage: ((clientId: Int, message: String) -> Unit)? = null
    var onClientConnected: ((clientId: Int, ip: String) -> Unit)? = null
    var onClientDisconnected: ((clientId: Int) -> Unit)? = null

    fun start(port: Int) {
        if (isRunning.get())
            error("Server is already running.")

        try {
            serverSocket = ServerSocket(port)
            isRunning.set(true)

            thread {
                acceptConnections()
            }
        } catch (e: Exception) {
            isRunning.set(false)
            error("Server startup error: ${e.message}")
        }
    }

    fun sendMessage(clientId: Int, message: String): Boolean {
        val client = clients[clientId] ?: return false
        client.writer.println(message)
        return true
    }

    fun broadcastMessage(message: String) {
        clients.values.forEach { it.writer.println(message) }
    }

    fun disconnectClient(clientId: Int) {
        val client = clients.remove(clientId)
        if (client != null) {
            try { client.socket.close() } catch (_: Exception) {  }
            onClientDisconnected?.invoke(clientId)
        }
    }

    fun stop() {
        isRunning.set(false)
        try { serverSocket?.close() } catch (_: Exception) {  }

        clients.keys().toList().forEach { disconnectClient(it) }
    }

    private fun acceptConnections() {
        while (isRunning.get()) {
            try {
                val socket = serverSocket?.accept() ?: break
                val ip = socket.inetAddress.hostAddress
                val clientId = idCounter.getAndIncrement()

                val writer = PrintWriter(socket.getOutputStream(), true)
                val connection = ClientConnection(clientId, socket, writer, ip)

                clients[clientId] = connection
                onClientConnected?.invoke(clientId, ip)

                thread {
                    handleClientMessages(connection)
                }
            } catch (e: Exception) {
                if (isRunning.get()) {
                    println("Connection accept error: ${e.message}")
                }
            }
        }
    }

    private fun handleClientMessages(client: ClientConnection) {
        try {
            val input = BufferedReader(InputStreamReader(client.socket.getInputStream()))
            var message: String?
            while (input.readLine().also { message = it } != null) {
                message?.let { onMessage?.invoke(client.id, it) }
            }
        } catch (e: Exception) {
            // Ошибка чтения (клиент разорвал соединение)
        } finally {
            disconnectClient(client.id)
        }
    }
}

data class ClientConnection(val id: Int, val socket: Socket, val writer: PrintWriter, val ip: String)
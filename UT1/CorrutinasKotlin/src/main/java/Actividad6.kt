import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

fun main() = runBlocking {
    println("Sistema iniciado")
    val job = launch(Dispatchers.Default) {
        try {
            var id = 1
            while (isActive) {
                println("Notificación #$id enviada")
                if (id == 5) throw Exception("Servidor de notificaciones caído")
                delay(1000)
                id++
            }
        } catch (e: CancellationException) {
            println("Servicio detenido correctamente")
        } catch (e: Exception) {
            println("Error crítico: ${e.message}")
        }
    }

    delay(3500)
    println("Cerrando sesión...")
    job.cancelAndJoin()
}

class NotificationTest {
    @Test
    fun testJobCancelado() = runTest {
        val job = launch {
            while (isActive) {
                delay(100)
            }
        }
        job.cancel()
        assertTrue(job.isCancelled)
    }
}
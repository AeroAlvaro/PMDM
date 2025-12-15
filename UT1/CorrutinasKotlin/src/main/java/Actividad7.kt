import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

fun main() = runBlocking {
    val job = launch(Dispatchers.Default) {
        try {
            repeat(100) { i ->
                if (!isActive) return@repeat
                println("Procesando lote $i...")
                delay(500)
            }
        } catch (e: CancellationException) {
            println("Tarea cancelada: ${e.message}")
        } finally {
            withContext(NonCancellable) {
                println("Cerrando recursos críticos...")
                delay(1000)
                println("Limpieza finalizada")
            }
        }
    }

    delay(1200)
    println("Solicitando cancelación desde UI...")
    job.cancel(CancellationException("Cancelado por el usuario"))
    job.join()
}

class TaskManagerTest {
    @Test
    fun testCancelacionYLimpieza() = runTest {
        val job = launch {
            try {
                delay(10000)
            } finally {
                withContext(NonCancellable) {
                    delay(100)
                }
            }
        }
        job.cancel()
        job.join()
        assertTrue(job.isCancelled)
    }
}
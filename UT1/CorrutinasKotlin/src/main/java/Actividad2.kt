import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

fun main() = runBlocking {
    println("App iniciada")

    val job = launch(Dispatchers.Default) {
        try {
            var contador = 0
            while (isActive) {
                contador++
                println("Timer: $contador seg")
                delay(1000)
                if (contador == 10) throw Exception("Fallo crítico simulado")
            }
        } catch (e: CancellationException) {
            println("Limpieza: El temporizador fue cancelado")
        } catch (e: Exception) {
            println("Error capturado: ${e.message}")
        }
    }

    delay(3500)
    println("Hilo principal realizando otras tareas...")

    job.cancelAndJoin()
    println("App finalizada")
}

class TimerTest {
    @Test
    fun testCancelacion() = runTest {
        val job = launch {
            delay(10000)
        }
        job.cancel()
        assertTrue(job.isCancelled)
    }
}
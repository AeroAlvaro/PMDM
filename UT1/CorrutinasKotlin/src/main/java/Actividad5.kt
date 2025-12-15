import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis
import kotlin.test.assertTrue

fun main() = runBlocking {
    val archivos = listOf("foto.jpg", "documento.pdf", "error_log.txt", "musica.mp3")

    val tiempo = measureTimeMillis {
        val jobs = archivos.map { archivo ->
            launch(Dispatchers.IO) {
                descargarArchivo(archivo)
            }
        }
        jobs.joinAll()
    }
    println("Operación masiva finalizada en $tiempo ms")
}

suspend fun descargarArchivo(nombre: String) {
    try {
        for (progreso in 25..100 step 25) {
            delay(100)
            if (nombre == "error_log.txt" && progreso == 50) {
                throw Exception("Conexión interrumpida")
            }
            println("Descargando $nombre... $progreso%")
        }
        println("$nombre guardado")
    } catch (e: Exception) {
        println("Error en $nombre: ${e.message}")
    }
}

class DownloaderTest {
    @Test
    fun testDescargaCompleta() = runTest {
        val job = launch {
            descargarArchivo("test_file.zip")
        }
        job.join()
        assertTrue(job.isCompleted)
    }
}
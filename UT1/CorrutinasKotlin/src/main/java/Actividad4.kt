import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals

fun main() = runBlocking {
    val tiempo = measureTimeMillis {
        try {
            val temperatura = async { obtenerTemperatura() }
            val humedad = async { obtenerHumedad() }
            val viento = async { obtenerViento(simularError = false) }

            println("Clima: ${temperatura.await()}, ${humedad.await()}, ${viento.await()}")
        } catch (e: Exception) {
            println("Error al obtener el clima: ${e.message}")
        }
    }
    println("Tiempo total concurrente: $tiempo ms")
}

suspend fun obtenerTemperatura(): String = withContext(Dispatchers.IO) {
    delay(1000)
    "25°C"
}

suspend fun obtenerHumedad(): String = withContext(Dispatchers.IO) {
    delay(1000)
    "60%"
}

suspend fun obtenerViento(simularError: Boolean): String = withContext(Dispatchers.IO) {
    delay(1000)
    if (simularError) throw Exception("Fallo en sensor de viento")
    "15 km/h NO"
}

class WeatherTest {
    @Test
    fun testCargaParalela() = runTest {
        val temp = async { obtenerTemperatura() }
        val hum = async { obtenerHumedad() }
        val viento = async { obtenerViento(false) }

        assertEquals("25°C", temp.await())
        assertEquals("60%", hum.await())
        assertEquals("15 km/h NO", viento.await())
    }
}
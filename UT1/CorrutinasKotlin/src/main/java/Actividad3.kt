import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

fun main() = runBlocking {
    val tiempo = measureTimeMillis {
        try {
            val resultado = obtenerDatosApi(simularError = false)
            println("Datos recibidos: $resultado")
        } catch (e: Exception) {
            println("Error en la petición: ${e.message}")
        }
    }
    println("Duración de la llamada: $tiempo ms")
}

suspend fun obtenerDatosApi(simularError: Boolean): String = withContext(Dispatchers.IO) {
    delay(2000)
    if (simularError) throw Exception("Error 500: Servidor no disponible")
    "{ \"usuario\": \"DevKotlin\", \"id\": 101 }"
}

class ApiTest {
    @Test
    fun testObtenerDatosExitoso() = runTest {
        val datos = obtenerDatosApi(simularError = false)
        assertEquals("{ \"usuario\": \"DevKotlin\", \"id\": 101 }", datos)
    }

    @Test
    fun testObtenerDatosFallo() = runTest {
        assertFailsWith<Exception> {
            obtenerDatosApi(simularError = true)
        }
    }
}
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.system.measureTimeMillis
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

fun main(): kotlin.Unit = runBlocking {
    val time = measureTimeMillis {
        try {
            val token = login("usuario", "password")
            val perfil = cargarPerfil(token)
            cargarPreferencias(perfil)
            println("Proceso finalizado correctamente")
        } catch (e: Exception) {
            println("Error capturado: ${e.message}")
        }
    }
    println("Tiempo total: $time ms")
}

suspend fun login(user: String, pass: String): String = withContext(Dispatchers.IO) {
    delay(1000)
    if (user.isEmpty()) throw Exception("Usuario inválido")
    "TOKEN_123"
}

suspend fun cargarPerfil(token: String): String = withContext(Dispatchers.IO) {
    delay(1000)
    "ID_USER_99"
}

suspend fun cargarPreferencias(id: String) = withContext(Dispatchers.IO) {
    delay(1000)
}

class AuthTest {
    @Test
    fun testFlujoCompleto() = runTest {
        val token = login("test", "pass")
        assertEquals("TOKEN_123", token)
        val perfil = cargarPerfil(token)
        assertEquals("ID_USER_99", perfil)
    }
}
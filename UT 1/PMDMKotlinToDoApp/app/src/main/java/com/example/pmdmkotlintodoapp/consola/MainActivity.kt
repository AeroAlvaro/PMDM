package com.example.pmdmkotlintodoapp.consola

class Tarea(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    var completada: Boolean = false
) {
    fun marcarComoCompletada() {
        completada = true
    }

    fun mostrarInfo() {
        val check = if (completada) "[✔]" else "[ ]"
        println("$check $id - $titulo ($descripcion)")
    }
}

class ListaTareas {
    private val tareas: MutableList<Tarea> = mutableListOf()

    fun agregarTarea(tarea: Tarea) {
        tareas.add(tarea)
        println("Tarea '${tarea.titulo}' agregada correctamente.\n")
    }

    fun eliminarTarea(id: Int) {
        val tarea = buscarTarea(id)
        if (tarea != null) {
            tareas.remove(tarea)
            println("Tarea '${tarea.titulo}' eliminada.\n")
        } else {
            println("No se encontró una tarea con id $id.\n")
        }
    }

    fun mostrarTareas() {
        if (tareas.isEmpty()) {
            println("No hay tareas registradas.\n")
        } else {
            println("\nLista de tareas:")
            tareas.forEach { it.mostrarInfo() }
            println()
        }
    }

    fun buscarTarea(id: Int): Tarea? {
        return tareas.find { it.id == id }
    }

    fun tareasCompletadas() {
        val completadas = tareas.filter { it.completada }
        if (completadas.isEmpty()) {
            println("No hay tareas completadas.\n")
        } else {
            println("\nTareas completadas:")
            completadas.forEach { it.mostrarInfo() }
            println()
        }
    }

    fun marcarTareaComoCompletada(id: Int) {
        val tarea = buscarTarea(id)
        if (tarea != null) {
            tarea.marcarComoCompletada()
            println("Tarea '${tarea.titulo}' marcada como completada.\n")
        } else {
            println("No se encontró una tarea con id $id.\n")
        }
    }
}

class Usuario(
    val nombre: String,
    val lista: ListaTareas
) {
    fun saludar() {
        println("\nHola, soy $nombre. Estas son mis tareas pendientes:\n")
        lista.mostrarTareas()
    }
}

fun main() {
    println("Ingresa tu nombre:")
    val nombre = readLine() ?: "Usuario"
    val usuario = Usuario(nombre, ListaTareas())

    var opcion: Int

    do {
        println("""
        
        GESTIÓN DE TAREAS
        
        1. Agregar tarea
        2. Mostrar tareas
        3. Marcar tarea como completada
        4. Eliminar tarea
        5. Ver solo tareas completadas
        6. Salir
        
        Selecciona una opción:
        """.trimIndent())

        opcion = readLine()?.toIntOrNull() ?: 0

        when (opcion) {
            1 -> {
                println("Ingresa el id de la tarea:")
                val id = readLine()?.toIntOrNull() ?: 0
                println("Título de la tarea:")
                val titulo = readLine() ?: ""
                println("Descripción de la tarea:")
                val descripcion = readLine() ?: ""
                usuario.lista.agregarTarea(Tarea(id, titulo, descripcion))
            }
            2 -> usuario.lista.mostrarTareas()
            3 -> {
                println("Ingresa el id de la tarea a marcar como completada:")
                val id = readLine()?.toIntOrNull() ?: 0
                usuario.lista.marcarTareaComoCompletada(id)
            }
            4 -> {
                println("Ingresa el id de la tarea a eliminar:")
                val id = readLine()?.toIntOrNull() ?: 0
                usuario.lista.eliminarTarea(id)
            }
            5 -> usuario.lista.tareasCompletadas()
            6 -> println("Saliendo...")
            else -> println("Opción no válida. Intenta de nuevo.\n")
        }
    } while (opcion != 6)
}
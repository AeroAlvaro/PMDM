package com.example.proyectoprimerospasos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random
import android.text.Html
import android.os.Build

class MainActivity : AppCompatActivity() {

    private lateinit var tvAlumnoSeleccionado: TextView
    private lateinit var tvListaAlumnos: TextView
    private lateinit var btnSiguiente: Button

    private var alumnosPorSalir = mutableListOf<String>()
    private var alumnosQueYaSalieron = mutableListOf<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAlumnoSeleccionado = findViewById(R.id.tvAlumnoSeleccionado)
        tvListaAlumnos = findViewById(R.id.tvListaAlumnos)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        alumnosPorSalir.addAll(listOf("Álvaro", "Carlos", "Santiago", "Helena"))

        actualizarListaVisual()

        btnSiguiente.setOnClickListener {
            seleccionarSiguiente()
        }
    }

    private fun seleccionarSiguiente() {
        if (alumnosPorSalir.isEmpty()) {
            reiniciarSorteo()
            return
        }

        val indexAleatorio = Random.nextInt(alumnosPorSalir.size)

        val alumnoElegido = alumnosPorSalir.removeAt(indexAleatorio)
        alumnosQueYaSalieron.add(alumnoElegido)

        tvAlumnoSeleccionado.text = alumnoElegido

        actualizarListaVisual()
    }

    private fun actualizarListaVisual() {
        val textoBuilder = StringBuilder()
        textoBuilder.append("Alumnos:\n\n")

        alumnosQueYaSalieron.forEach { alumno ->
            val textoAlumno = "[X] $alumno\n"
            textoBuilder.append(textoAlumno)
        }

        alumnosPorSalir.forEach { alumno ->
            textoBuilder.append("[ ] $alumno\n")
        }

        tvListaAlumnos.text = textoBuilder.toString()
    }

    private fun reiniciarSorteo() {
        alumnosPorSalir.addAll(alumnosQueYaSalieron)
        alumnosQueYaSalieron.clear()
        actualizarListaVisual()
        tvAlumnoSeleccionado.text = "Lista reiniciada"
    }
}
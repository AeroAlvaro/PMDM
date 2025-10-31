package com.example.proyectoprimerospasos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var tvAlumnoSeleccionado: TextView
    private lateinit var tvListaAlumnos: TextView
    private lateinit var btnSiguiente: Button

    private var alumnosLista = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvAlumnoSeleccionado = findViewById(R.id.tvAlumnoSeleccionado)
        tvListaAlumnos = findViewById(R.id.tvListaAlumnos)
        btnSiguiente = findViewById(R.id.btnSiguiente)

        alumnosLista.addAll(listOf("Álvaro", "Carlos", "Santiago", "Helena"))

        actualizarListaVisual()

        btnSiguiente.setOnClickListener {
            seleccionarSiguiente()
        }
    }

    private fun seleccionarSiguiente() {
        val indexAleatorio = Random.nextInt(alumnosLista.size)
        val alumnoElegido = alumnosLista[indexAleatorio]
        tvAlumnoSeleccionado.text = alumnoElegido
    }

    private fun actualizarListaVisual() {
        val textoBuilder = StringBuilder()
        textoBuilder.append("Alumnos:\n\n")

        alumnosLista.forEach { alumno ->
            textoBuilder.append("$alumno\n")
        }

        tvListaAlumnos.text = textoBuilder.toString()
    }
}
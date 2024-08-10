package com.example.unscramble.ui

data class GameUiState(
    val palabraCodificadaActual: String = "",
    val esPalabraAdivinadaIncorrecta: Boolean = false,
    val puntaje: Int = 0,
    val cantidadPalabrasUtilizadas: Int = 1,
    val juegoTerminado: Boolean = false
)

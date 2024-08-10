package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.palabras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var palabrasUsadas: MutableSet<String> = mutableSetOf()
    private lateinit var palabraActual: String

    var palabraAdivinadaUsuario by mutableStateOf("")
        private set

    init {
        resetGame()
    }

    fun resetGame() {
        palabrasUsadas.clear()
        _uiState.value = GameUiState(palabraCodificadaActual = elegirPalabraAleatoria())
    }

    private fun elegirPalabraAleatoria(): String {
        palabraActual = palabras.random()
        if (palabrasUsadas.contains(palabraActual)) {
            return elegirPalabraAleatoria()
        } else {
            palabrasUsadas.add(palabraActual)
            return barajarPalabraActual(palabraActual)
        }
    }

    private fun barajarPalabraActual(palabra: String): String {
        val palabraTemporal = palabra.toCharArray()
        // Scramble the word
        palabraTemporal.shuffle()
        while (String(palabraTemporal).equals(palabra)) {
            palabraTemporal.shuffle()
        }
        return String(palabraTemporal)
    }

    fun actualizarpalabraAdivinadaUsuario(palabraAdivinada: String) {
        palabraAdivinadaUsuario = palabraAdivinada
    }

    fun verificarPalabraXUsuario() {
        if (palabraAdivinadaUsuario.equals(palabraActual, ignoreCase = true)) {
            val actualizarPuntaje = _uiState.value.puntaje.plus(SCORE_INCREASE)

            actualizarEstadoJuego(actualizarPuntaje)

        } else {
            _uiState.update { currentState -> currentState.copy(esPalabraAdivinadaIncorrecta = true) }
        }
        actualizarpalabraAdivinadaUsuario("")
    }

    private fun actualizarEstadoJuego(actualizarPuntaje: Int) {

        if (palabrasUsadas.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    esPalabraAdivinadaIncorrecta = false,
                    puntaje = actualizarPuntaje,
                    juegoTerminado = true
                )
            }


        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    esPalabraAdivinadaIncorrecta = false,
                    palabraCodificadaActual = elegirPalabraAleatoria(),
                    puntaje = actualizarPuntaje,
                    cantidadPalabrasUtilizadas = currentState.cantidadPalabrasUtilizadas.inc()
                )
            }
        }
    }

    fun saltarPalabra() {
        actualizarEstadoJuego(_uiState.value.puntaje)
        actualizarpalabraAdivinadaUsuario("")
    }
}


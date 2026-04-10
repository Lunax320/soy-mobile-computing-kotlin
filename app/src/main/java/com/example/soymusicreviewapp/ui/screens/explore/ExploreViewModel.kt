package com.example.soymusicreviewapp.ui.screens.explore

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.soymusicreviewapp.data.Song
import com.example.soymusicreviewapp.data.local.LocalSongsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(): ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())
    val uiState: StateFlow<ExploreState> = _uiState.asStateFlow()

    init {
        loadSongs()
    }

    fun onSearchChange(newText: String) {
        _uiState.update { currentState ->
            currentState.copy(searchText = newText)
        }
    }

    private fun loadSongs() {
        viewModelScope.launch {
            val backendSongs = withContext(Dispatchers.IO) {
                try {
                    Log.d("API_TRACKER", "Explorar: Solicitando catálogo de canciones al servidor...")
                    val response = URL("http://10.0.2.2:3000/songs").readText()
                    val jsonArray = JSONArray(response)
                    val list = mutableListOf<Song>()

                    for (i in 0 until jsonArray.length()) {
                        val obj = jsonArray.getJSONObject(i)

                        // Validación de imágenes para evitar espacios en blanco
                        var imgUrl = if (obj.isNull("songImage")) "" else obj.getString("songImage")
                        if (imgUrl.contains("example.com") || imgUrl.isEmpty()) {
                            imgUrl = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500&q=80"
                        }

                        list.add(
                            Song(
                                songId = obj.getInt("id").toString(),
                                name = obj.getString("name"),
                                artist = obj.getString("artist"),
                                genre = obj.optString("genre", "Desconocido"),
                                duration = obj.optString("duration", "0:00"),
                                songImage = imgUrl
                            )
                        )
                    }
                    Log.d("API_TRACKER", "Explorar: Catálogo actualizado con ${list.size} canciones.")
                    list
                } catch (e: Exception) {
                    Log.e("API_TRACKER", "Explorar: Error de conexión. Cargando catálogo de emergencia. Motivo: ${e.message}")
                    LocalSongsProvider.songs
                }
            }

            _uiState.update { currentState ->
                currentState.copy(songs = backendSongs)
            }
        }
    }
}
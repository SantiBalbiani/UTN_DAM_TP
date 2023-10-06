package ar.edu.utn.frba.placesify.model

import android.content.Context
import android.content.SharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("PreferenciasPlacesify", Context.MODE_PRIVATE)

    fun saveData(key: String, value: Lugares, empty: Boolean = false) {

        val editor = sharedPreferences.edit()

        // Si se desea vaciarla
        if (empty) {
            editor.putString(key, Json.encodeToString(ArrayList<Lugares>()))

        } else {
            // Creo una Lista Auxiliar para cargarla con los elementos actuales del SharedPreferences
            var listaAux = getData(key, ArrayList<Lugares>())

            // Agrego el nuevo elemento
            if (listaAux != null) {
                listaAux.add(value)
            }

            editor.putString(key, Json.encodeToString(listaAux))
        }

        editor.apply()
    }

    fun getData(key: String, defaultValue: ArrayList<Lugares>): ArrayList<Lugares>? {
        return sharedPreferences.getString(key, defaultValue.toString())
            ?.let { Json.decodeFromString(it) }
    }
}
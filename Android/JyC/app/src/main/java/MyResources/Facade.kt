package MyResources

import android.content.Context
import android.content.SharedPreferences
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private var preferenceKey: String = ""

class Facade {

    fun setPreferenceKey(context: Context, keyPref: String?, valor: String?) {
        val preferences = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor
        editor = preferences.edit()
        editor.putString(keyPref, valor)
        editor.commit()
    }

    fun getPreferenceKey(context: Context, keyPref: String?): String? {
        val preferences = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        return preferences.getString(keyPref, "")
    }

    fun deletePreferenceKey(context: Context, keyPref: String?) {
        val preferences = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor
        editor = preferences.edit()
        editor.clear()
        editor.commit()
    }

    fun getDateTime(): String? {
        val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss")
        val date = Date()
        return dateFormat.format(date)
    }
}
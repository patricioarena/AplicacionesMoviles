package MyResources

import android.content.Context
import android.content.SharedPreferences

private var preferenceKey: String = ""

class Storage {

    fun setPreferenceKey(context: Context, keyPref: String?, valor: String?) {
        val settings = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor
        editor = settings.edit()
        editor.putString(keyPref, valor)
        editor.commit()
    }

    fun getPreferenceKey(context: Context, keyPref: String?): String? {
        val preferences = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE)
        return preferences.getString(keyPref, "")
    }


}
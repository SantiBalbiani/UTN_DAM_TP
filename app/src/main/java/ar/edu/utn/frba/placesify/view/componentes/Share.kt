package ar.edu.utn.frba.placesify.view.componentes

import android.content.Intent

fun SharePlainText(subject: String, type: String = "text/plain", extraText: String = "") : Intent {

    val intent = Intent(Intent.ACTION_SEND)
    intent.type = type
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, extraText)

    return intent
}


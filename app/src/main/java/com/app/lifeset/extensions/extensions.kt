package com.app.lifeset.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.app.lifeset.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // For 29 api or above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    // For below 29 api
    else {
        if (connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!.isConnectedOrConnecting) {
            return true
        }
    }
    return false
}


fun createdDateFormat(inputDate: String): String {
    val input = inputDate
    val correctedInput = input.uppercase(Locale.ENGLISH) // converts "am" to "AM"

    val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
    val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)

    val date = LocalDateTime.parse(correctedInput, inputFormatter)
    val formattedDate = date.format(outputFormatter)
    return formattedDate

}



fun isValidPhoneNumber(context: Context, edtPhoneNumber: String): Boolean {
    var isValid = true
    if (edtPhoneNumber.isEmpty() || edtPhoneNumber.length < 10) {
        Toast.makeText(
            context,
            context.getString(R.string.str_error_valid_phone_number),
            Toast.LENGTH_SHORT
        ).show()
        isValid = false
    }
    return isValid
}


fun isValidateEmail(context: Context, edtEmail: String): Boolean {
    var isValid = true
    if (edtEmail.isEmpty()) {
        Toast.makeText(
            context,
            context.getString(R.string.str_error_email_address),
            Toast.LENGTH_SHORT
        ).show()
        isValid = false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(edtEmail.toString().trim { it <= ' ' })
            .matches()
    ) {
        Toast.makeText(
            context,
            context.getString(R.string.str_error_valid_email_address),
            Toast.LENGTH_SHORT
        ).show()

        isValid = false
    }
    return isValid
}


fun isValidPassword(context: Context, edtPassword: String): Boolean {
    var isValid = true
    if (edtPassword.isEmpty()) {
        Toast.makeText(
            context,
            context.getString(R.string.str_error_password),
            Toast.LENGTH_SHORT
        ).show()
        isValid = false
    }
    return isValid
}

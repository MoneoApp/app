package ml.moneo.app.util

import android.app.Activity
import android.content.Context
import android.content.Intent

fun openActivity(context: Context, activity: Class<out Activity>, replace: Boolean = false) {
    val intent = Intent(context, activity)

    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    context.startActivity(intent)

    if (replace) {
        (context as Activity).finish()
    }
}

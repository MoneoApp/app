package ml.moneo.app.view.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QRSwitch(default: Boolean, onSwitched: (Boolean) -> Unit) {
    var switched by remember { mutableStateOf(default) }

    Row {
        Icon(
            Icons.Filled.SettingsRemote,
            contentDescription = "See help image",
            tint = MaterialTheme.colors.onBackground
        )
        Switch(
            checked = switched,
            onCheckedChange =
            {
                switched = it
                onSwitched(it)
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colors.onBackground,
                uncheckedTrackColor = MaterialTheme.colors.onBackground,
                checkedThumbColor = MaterialTheme.colors.onBackground,
                uncheckedThumbColor = MaterialTheme.colors.onBackground
            )
        )
        Icon(
            Icons.Filled.QrCode,
            contentDescription = "See help image",
            tint = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(start = 2.dp)
        )
    }
}

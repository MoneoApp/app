package ml.moneo.app.view.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import ml.moneo.app.view.theme.Theme

@Composable
fun AnchorPopup(
    theme: String,
    imageUrl: String
) {
    var expanded by remember { mutableStateOf(false) }
    val painter = rememberCoilPainter(request = imageUrl)

    Theme(theme) {
        Surface(
            color = Color.Transparent,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Box(
                    modifier = Modifier.padding(16.dp)
                ) {
                    IconButton(
                        onClick = { expanded = true }
                    ) {
                        Icon(
                            Icons.Filled.HelpOutline,
                            contentDescription = "See help image",
                            tint = Color.White
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.background)
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "Help image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 16.dp,
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp
                                ),
                            contentScale = ContentScale.FillWidth)
                        Text("Scan dit gedeelte van uw apparaat",
                            Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(bottom = 8.dp))
                    }
                }
            }
        }
    }
}
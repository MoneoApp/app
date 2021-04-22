package ml.moneo.app.view

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ml.moneo.app.activity.MainActivity
import ml.moneo.app.util.openActivity
import ml.moneo.app.view.theme.Yellow
import ml.moneo.app.R

@Composable
fun OnboardingView() {
    val context = LocalContext.current
    val request = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { success ->
        if (!success) {
            Toast.makeText(context, R.string.camera_error, Toast.LENGTH_SHORT).show()
        } else {
            openActivity(context, MainActivity::class.java, true)
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(6f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(200.dp)
                    .background(Yellow),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null
            )
            Text(
                modifier = Modifier.padding(top = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                text = "MONEO",
                color = MaterialTheme.colors.onBackground
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "To begin, please grant camera permissions",
                color = MaterialTheme.colors.onBackground,
                fontSize = 14.sp
            )
            TextButton(
                onClick = { request.launch(Manifest.permission.CAMERA) },
                shape = CircleShape,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Start",
                    fontSize = 20.sp
                )
            }
        }
    }
}

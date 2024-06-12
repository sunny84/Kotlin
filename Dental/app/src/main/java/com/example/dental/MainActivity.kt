package com.example.dental

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    companion object {
        private const val mediaPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val imagePermission = android.Manifest.permission.READ_MEDIA_IMAGES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var imageUri by remember { mutableStateOf<Uri?>(null) }
            var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

            val context = LocalContext.current

            SelectImage(
                imageUri = imageUri,
                imageBitmap = imageBitmap,
                onImageSelected = { uri ->
                    imageUri = uri
                    imageBitmap = uri?.toBitmap(context)
                },
                onCrop = { imageBitmap = cropImage(imageBitmap) },
                onGrayscale = { imageBitmap = grayscaleImage(imageBitmap) },
                onBrighten = { imageBitmap = adjustBrightness(imageBitmap, 1.2f) },
                onDarken = { imageBitmap = adjustBrightness(imageBitmap, 0.8f) },
                onZoomIn = { imageBitmap = zoomImage(imageBitmap, 1.2f) },
                onZoomOut = { imageBitmap = zoomImage(imageBitmap, 0.8f) }
            )

            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent(),
                onResult = { uri ->
                    imageUri = uri
                    imageBitmap = uri?.toBitmap(context)
                }
            )
        }
    }

    private fun requestStoragePermission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(imagePermission)
        } else {
            arrayOf(mediaPermission)
        }
        checkPermissionsAndStartMotion(permissions)
    }

    private fun checkPermissionsAndStartMotion(permissions: Array<String>) {
        val permissionResults = permissions.map {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (permissionResults.all { it }) {
            pickMedia()
        } else {
            ActivityCompat.requestPermissions(this, permissions, 100)
        }
    }

    private fun pickMedia() {
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
//
//    LaunchedEffect(Unit) {
//        requestStoragePermission()
//    }
}
@Composable
fun SelectImage(
    imageUri: Uri?,
    imageBitmap: Bitmap?,
    onImageSelected: (Uri?) -> Unit,
    onCrop: () -> Unit,
    onGrayscale: () -> Unit,
    onBrighten: () -> Unit,
    onDarken: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = onImageSelected
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image display area
        imageBitmap?.let { bitmap ->
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
        }

        // Image selection button
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Image")
        }

        // Image manipulation buttons
        Row(modifier = Modifier.padding(top = 16.dp)) {
            Button(onClick = onCrop) {
                Text("Crop")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onGrayscale) {
                Text("Grayscale")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onBrighten) {
                Text("Brighten")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onDarken) {
                Text("Darken")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onZoomIn) {
                Text("Zoom In")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onZoomOut) {
                Text("Zoom Out")
            }
        }
    }
}

private fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, this)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, this)
            val hardwareBitmap = ImageDecoder.decodeBitmap(source)
            hardwareBitmap.copy(Bitmap.Config.ARGB_8888, true)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun cropImage(bitmap: Bitmap?): Bitmap? {
    return try {
        bitmap ?: return null
        val width = bitmap.width
        val height = bitmap.height
        Bitmap.createBitmap(bitmap, width / 4, height / 4, width / 2, height / 2)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun grayscaleImage(bitmap: Bitmap?): Bitmap? {
    return try {
        bitmap ?: return null
        val width = bitmap.width
        val height = bitmap.height
        val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscaleBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix()
        colorMatrix.setSaturation(0f)
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        grayscaleBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun adjustBrightness(bitmap: Bitmap?, factor: Float): Bitmap? {
    return try {
        bitmap ?: return null
        val width = bitmap.width
        val height = bitmap.height
        val brightnessBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(brightnessBitmap)
        val paint = Paint()
        val colorMatrix = ColorMatrix(floatArrayOf(
            factor, 0f, 0f, 0f, 0f,
            0f, factor, 0f, 0f, 0f,
            0f, 0f, factor, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        ))
        val colorFilter = ColorMatrixColorFilter(colorMatrix)
        paint.colorFilter = colorFilter
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        brightnessBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun zoomImage(bitmap: Bitmap?, factor: Float): Bitmap? {
    return try {
        bitmap ?: return null
        val width = (bitmap.width * factor).toInt()
        val height = (bitmap.height * factor).toInt()
        Bitmap.createScaledBitmap(bitmap, width, height, true)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : ComponentActivity() {
    companion object {
        private const val mediaPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        private const val imagePermission = android.Manifest.permission.READ_MEDIA_IMAGES
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var navController = rememberNavController()
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
            var savedImageUri by remember { mutableStateOf<Uri?>(null) }

            val context = LocalContext.current
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    SelectImageScreen(
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
                        onZoomOut = { imageBitmap = zoomImage(imageBitmap, 0.8f) },
                        onSaveImage = { savedImageUri = saveImage(context, imageBitmap) },
                        onReloadImage = { imageBitmap = savedImageUri?.toBitmap(context) },
                        onDrawGraph = { navController.navigate("graph") }
                    )
                }
                composable("graph") {
                    GraphScreen(imageBitmap = imageBitmap)
                }
            }

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
fun SelectImageScreen(
    imageUri: Uri?,
    imageBitmap: Bitmap?,
    onImageSelected: (Uri?) -> Unit,
    onCrop: () -> Unit,
    onGrayscale: () -> Unit,
    onBrighten: () -> Unit,
    onDarken: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onSaveImage: () -> Unit,
    onReloadImage: () -> Unit,
    onDrawGraph: () -> Unit
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

        Row(){
            // Image selection button
            Button(onClick = { launcher.launch("image/*") }) {
                Text("Select Image")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onSaveImage) {
                Text("Save Image")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onReloadImage) {
                Text("Reload Saved Image")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onDrawGraph) {
                Text("Draw Graph")
            }
        }

        Row(
        ) {
            // Image display area
            imageBitmap?.let { bitmap ->
                Image(bitmap = bitmap.asImageBitmap(), contentDescription = null)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                
            ) {
                // Image manipulation buttons
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Button(onClick = onCrop) {
                        Text("Crop")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onGrayscale) {
                        Text("Grayscale")
                    }
                }
                Row(modifier = Modifier.padding(top = 16.dp)) {
                    Button(onClick = onBrighten) {
                        Text("Brighten")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onDarken) {
                        Text("Darken")
                    }
                }
                Row(modifier = Modifier.padding(top = 16.dp)) {
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
    }
}
@Composable
fun GraphScreen(imageBitmap: Bitmap?) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        imageBitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = null)
        }

        Canvas(modifier = Modifier
            .width(300.dp)
            .height(300.dp)
        ) {
            drawGraph(imageBitmap)
        }
    }
}

private fun DrawScope.drawGraph(bitmap: Bitmap?) {
    bitmap ?: return

    val pixelValues = IntArray(256)
    for (x in 0 until bitmap.width) {
        for (y in 0 until bitmap.height) {
            val pixel = bitmap.getPixel(x, y)
            val brightness = (
                    0.299 * android.graphics.Color.red(pixel) +
                    0.587 * android.graphics.Color.green(pixel) +
                    0.114 * android.graphics.Color.blue(pixel)).toInt()
            pixelValues[brightness]++
        }
    }

    val maxPixelValue = pixelValues.maxOrNull() ?: 1
    val scaleX = size.width / 256
    val scaleY = size.height / maxPixelValue

    for (i in pixelValues.indices) {
        val barHeight = pixelValues[i] * scaleY
        drawRect(
            color = Color.Black,
            topLeft = Offset(i * scaleX, size.height - barHeight),
            size = Size(scaleX, barHeight)
        )
    }

    // Draw x-axis values
    for (i in 0..255 step 32) {
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                i.toString(),
                i * scaleX,
                size.height - 10,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 20f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }

    // Draw y-axis values
    val numberOfYLabels = 5
    for (i in 0..numberOfYLabels) {
        val yValue = (maxPixelValue / numberOfYLabels) * i
        val yPos = size.height - (yValue * scaleY)

        drawLine(
            start = androidx.compose.ui.geometry.Offset(0f, yPos),
            end = androidx.compose.ui.geometry.Offset(size.width, yPos),
            color = Color.Gray,
            strokeWidth = 1f
        )

        drawContext.canvas.nativeCanvas.apply {
            drawText(
                yValue.toString(),
                10f,
                yPos,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 20f
                    textAlign = android.graphics.Paint.Align.RIGHT
                }
            )
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

private fun saveImage(context: Context, bitmap: Bitmap?): Uri? {
    if(bitmap == null) return null

    val filename = "edited_image.png"
    val file = File(context.cacheDir, filename)
    return try {
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        FileProvider.getUriForFile(context, "${context.packageName}.file-provider", file)
        // You can use the saveUri to share the image or open it in another app
        //Log.d("SaveImage", "Image saved: $savedUri")
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e("SaveImage", "Error saving image: ${e.message}")
        null
    }
}

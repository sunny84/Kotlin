package com.example.qrcodereader

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.qrcodereader.ui.theme.QRCodeReaderTheme
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import android.Manifest
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.google.common.util.concurrent.ListenableFuture

class MainActivity : ComponentActivity() {

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CameraPreviewScreen()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}

@Composable
fun CameraPreviewScreen() {
    // Compose에서 현재 컨텍스트(Context)를 가져옵니다.
    val context = LocalContext.current
    // 현재 컴포저블 함수 내에서 'CameraViewModel'의 인스턴스를 생성합니다.
    // 이 뷰모델 인스턴스는 'CameraPreviewScreen'의 라이프사이클 동안 지속됩니다.
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture: State<ListenableFuture<ProcessCameraProvider>> = remember {
        mutableStateOf(ProcessCameraProvider.getInstance(context))
    }

    val hasCameraPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            hasCameraPermission.value = true
            Toast.makeText(context, "권한 요청이 승인되었습니다.", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, "권한 요청이 거부되었습니다.", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        val cameraProvider = cameraProviderFuture.value.get()
        val preview = CameraPreview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as ComponentActivity, cameraSelector, preview
            )
        } catch (exc: Exception) {
            // Handle exceptions
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission.value) {
            AndroidView(factory = { previewView })
        }
    }
}

class CameraViewModel : ViewModel() {
    var previewView: PreviewView? = null
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    fun startCamera(context: Context) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider, context)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider, context: Context) {
        val preview = CameraPreview.Builder().build().also {
            it.setSurfaceProvider(previewView?.surfaceProvider)
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as ComponentActivity, cameraSelector, preview
            )
        } catch (exc: Exception) {
            // Handle exceptions
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QRCodeReaderTheme {
        Greeting("Android")
    }
}

@Composable
fun ActivityLayoutMain() {
    Column () {
        Button(
            onClick = { /*TODO*/ },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.filledTonalButtonColors(),
            modifier = Modifier.padding(10.dp)
                .size(width = 150.dp, height = 50.dp)
        ) {
            Text(text = "BINDING")
        }
        Button(
            onClick = { /*TODO*/ },
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.filledTonalButtonColors(),
            modifier = Modifier.padding(10.dp)
                .size(width = 150.dp, height = 50.dp)
        ) {
            Text(text = "NO BINDING")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActivityLayoutMainPreview() {
    QRCodeReaderTheme {
        ActivityLayoutMain()
    }
}
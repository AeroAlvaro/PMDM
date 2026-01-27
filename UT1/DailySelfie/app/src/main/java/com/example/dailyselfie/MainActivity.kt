package com.example.dailyselfie

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.work.WorkManager
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.ExistingPeriodicWorkPolicy

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scheduleSelfieReminder(this)

        setContent {
            DailySelfieApp()
        }
    }

    private fun scheduleSelfieReminder(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<SelfieWorker>(24, TimeUnit.HOURS)
            .addTag("daily_reminder")
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "daily_selfie_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}

@Composable
fun DailySelfieApp() {
    val context = LocalContext.current
    var isAuthenticated by remember { mutableStateOf(false) }
    var showBiometricPrompt by remember { mutableStateOf(true) }

    LaunchedEffect(showBiometricPrompt) {
        if (showBiometricPrompt && !isAuthenticated) {
            authenticateUser(context as FragmentActivity) { success ->
                isAuthenticated = success
                if (!success) {
                    Toast.makeText(context, "Autenticación requerida", Toast.LENGTH_SHORT).show()
                }
            }
            showBiometricPrompt = false
        }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

            AnimatedVisibility(
                visible = isAuthenticated,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically()
            ) {
                MainScreen()
            }

            if (!isAuthenticated) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Lock, contentDescription = "Locked", modifier = Modifier.size(64.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("App Bloqueada")
                        Button(onClick = { showBiometricPrompt = true }, modifier = Modifier.padding(top = 16.dp)) {
                            Text("Desbloquear")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val photoList = remember { mutableStateListOf<Uri>() }
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    fun loadPhotos() {
        photoList.clear()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                photoList.add(contentUri)
            }
        }
    }

    LaunchedEffect(Unit) {
        loadPhotos()
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            Toast.makeText(context, "¡Foto guardada!", Toast.LENGTH_SHORT).show()
            loadPhotos()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val cameraGranted = permissions[Manifest.permission.CAMERA] ?: false
        if (cameraGranted) {
            tempImageUri = createImageUri(context)
            tempImageUri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(context, "Permiso de cámara necesario", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daily Selfie") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val permissionsToRequest = mutableListOf(Manifest.permission.CAMERA)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
                    permissionsToRequest.add(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                }

                permissionLauncher.launch(permissionsToRequest.toTypedArray())
            }) {
                Icon(Icons.Default.CameraAlt, contentDescription = "Tomar Foto")
            }
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 128.dp),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(paddingValues).fillMaxSize()
        ) {
            items(photoList) { uri ->
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .aspectRatio(1f),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Selfie",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

fun authenticateUser(activity: FragmentActivity, onResult: (Boolean) -> Unit) {
    val executor: Executor = ContextCompat.getMainExecutor(activity)
    val biometricPrompt = BiometricPrompt(activity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onResult(true)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                onResult(false)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Autenticación Requerida")
        .setSubtitle("Usa tu huella o rostro para entrar")
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        .build()

    biometricPrompt.authenticate(promptInfo)
}

fun createImageUri(context: Context): Uri? {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/DailySelfie")
        }
    }

    return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
}
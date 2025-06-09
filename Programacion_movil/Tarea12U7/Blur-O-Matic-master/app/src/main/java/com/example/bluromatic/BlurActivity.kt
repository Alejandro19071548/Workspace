package com.example.bluromatic

// Importaciones necesarias para trabajar con componentes, URIs y Compose
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.bluromatic.ui.BluromaticScreen
import com.example.bluromatic.ui.theme.BluromaticTheme

class BlurActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Habilita que el contenido pueda extenderse bajo la barra de estado y navegación
        enableEdgeToEdge()

        // Llama al método onCreate del padre
        super.onCreate(savedInstanceState)

        // Establece el contenido de la UI usando Compose
        setContent {
            BluromaticTheme {
                Surface(
                    modifier = Modifier.fillMaxSize() // Ocupa toda la pantalla
                ) {
                    // Llama a la pantalla principal de Bluromatic
                    BluromaticScreen()
                }
            }
        }
    }
}


fun Context.getImageUri(): Uri {
    val resources = this.resources
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE) // Esquema para recursos internos
        .authority(resources.getResourcePackageName(R.drawable.android_cupcake)) // Nombre del paquete
        .appendPath(resources.getResourceTypeName(R.drawable.android_cupcake)) // Tipo de recurso (drawable)
        .appendPath(resources.getResourceEntryName(R.drawable.android_cupcake)) // Nombre del recurso (android_cupcake)
        .build() // Construye la URI final
}

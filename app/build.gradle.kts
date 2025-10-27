plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.myapplication"

    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // ✅ Aseguramos compatibilidad uniforme entre Java y Kotlin
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }


    // ✅ Activa Jetpack Compose
    buildFeatures {
        compose = true
    }

    // ✅ Versión del compilador Compose (debe coincidir con el BOM)

}

// ✅ Usa siempre JDK 17 (evita conflicto entre Java/Kotlin)
kotlin {
    jvmToolchain(17)
}

dependencies {
    // 🧩 Librerías base de Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // 🧩 Jetpack Compose (usando BOM para manejar versiones)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // 🧩 Navigation Compose
    implementation(libs.androidx.navigation.compose)

    // 🧩 ViewModel + State + Compose integration
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.compose.runtime)

    // 🧩 Room (con KSP)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.runtime.livedata)
    ksp(libs.androidx.room.compiler)

    // 🧩 Coil para carga de imágenes
    implementation(libs.coil.compose)

    // 🧪 Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    // 🧩 Herramientas de depuración
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

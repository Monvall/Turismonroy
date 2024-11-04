// build.gradle.kts (Nivel del Proyecto)

buildscript {
    val kotlin_version = "1.9.0" // Cambia a 1.9.0 para asegurar compatibilidad
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}


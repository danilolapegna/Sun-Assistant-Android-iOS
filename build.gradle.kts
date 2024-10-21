import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

plugins {
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.android.application").version("8.1.3").apply(false)
    id("com.android.library").version("8.1.3").apply(false)
    kotlin("android").version("1.9.10").apply(false)
    kotlin("multiplatform").version("1.9.10").apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("maven")
}

repositories {
    google()
    mavenCentral()
    jcenter()
}

dependencies {
    compileOnly(gradleApi())
    compileOnly(group = "com.android.tools.build", name = "gradle", version = ProjectConfig.Versions.androidPlugin)

    implementation(kotlin("stdlib"))

    implementation(group = "commons-io", name = "commons-io", version = "2.8.0")
    implementation(group = "org.ow2.asm", name = "asm", version = "9.1")
}

listOf("compileKotlin", "compileTestKotlin").forEach {
    tasks.getByName<KotlinCompile>(it) {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

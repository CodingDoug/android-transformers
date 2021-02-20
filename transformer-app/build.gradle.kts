buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath(kotlin("gradle-plugin", version = "1.4.30"))
        classpath("com.hyperaware.transformer:transformer-plugin:0.1")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
        mavenLocal()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

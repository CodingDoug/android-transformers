buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath(group = "com.android.tools.build", name = "gradle", version = ProjectConfig.Versions.androidPlugin)
        classpath(group = "com.github.dcendents", name = "android-maven-gradle-plugin", version = "2.1")
        classpath(kotlin("gradle-plugin", version = ProjectConfig.Versions.kotlin))
    }
}

allprojects {
   repositories {
       google()
       mavenCentral()
       jcenter()
   }

    group = ProjectConfig.Maven.group
    version = ProjectConfig.Maven.version
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

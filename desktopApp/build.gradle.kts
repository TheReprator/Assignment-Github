import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.compose.reload.gradle.ComposeHotRun

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.hotReload)
}

kotlin {
    jvm {
        @OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    sourceSets {
        jvmMain.dependencies {
            implementation(projects.sharedCode)
            implementation(compose.desktop.currentOs)
        }
    }
}

tasks.withType<ComposeHotRun>().configureEach {
    mainClass = "dev.reprator.github.MainKt"
}

compose.desktop {
    application {
        mainClass = "dev.reprator.github.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "dev.reprator.github"
            packageVersion = "1.0.0"
            includeAllModules = true

            linux {
                iconFile.set(project.file("resources/LinuxIcon.png"))
            }
            windows {
                iconFile.set(project.file("resources/WindowsIcon.ico"))
            }
            macOS {
                iconFile.set(project.file("resources/MacosIcon.icns"))
                bundleID = "org.company.app.desktopApp"
            }
        }

        buildTypes.release.proguard {
            configurationFiles.from("compose-desktop.pro")
        }
    }
}
import org.apache.commons.exec.CommandLine
import org.apache.commons.exec.DefaultExecutor
import java.io.File

object BuildFileGenerator {

    fun buildGeneratedCode(targetDir: File) {
        println("Start build for generated code")
        with(DefaultExecutor()) {
            setExitValue(0)
            workingDirectory = targetDir
            execute(CommandLine.parse("gradlew.bat -S build"))
        }
        println("Build for generated code has done.")
    }

    fun runGeneratedCode(targetDir: File) {
        with(DefaultExecutor()) {
            setExitValue(0)
            workingDirectory = targetDir
            execute(CommandLine.parse("gradlew.bat -S run --args=\"foo\""))
        }
    }

    fun generateBuildFiles(targetDir: File, projectRootDir: File) {
        targetDir.mkdirs()
        copyGradlewFiles(targetDir, projectRootDir)

        File(targetDir, "build.gradle.kts").apply {
            createNewFile()
            writeText(`get build-gradle-kts as test`())
        }
        File(targetDir, "settings.gradle.kts").apply {
            createNewFile()
            writeText(`get settings-gradle-kts as test`())
        }
    }

    private fun copyGradlewFiles(targetDir: File, projectRootDir: File) {
        File(projectRootDir, "gradlew").copyTo(File(targetDir, "gradlew"), true)
        File(projectRootDir, "gradlew.bat").copyTo(File(targetDir, "gradlew.bat"), true)
        File(projectRootDir, "gradle").copyRecursively(File(targetDir, "gradle"), true)
    }

    private fun `get settings-gradle-kts as test`() = """
        rootProject.name = "generated"
    """.trimIndent()

    private fun `get build-gradle-kts as test`() = """
        plugins {
            kotlin("jvm") version "1.3.72"
            application
        }
        
        repositories {
            mavenCentral()
        }
        
        dependencies {
            implementation(kotlin("stdlib"))
        }
        
        tasks {
            withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
                kotlinOptions.jvmTarget = "1.8"
            }
            withType<JavaExec> {
                setArgsString("foo")
            }
        }
        
        application {
            mainClassName = "dev.toliner.GreeterKt"
        }
    """.trimIndent()
}
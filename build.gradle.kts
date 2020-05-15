plugins {
    kotlin("jvm") version "1.3.72"
    application
}

group = "dev.toliner"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.squareup:kotlinpoet:1.5.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    val generateTargetDir = File(buildDir, "generated")
    withType<JavaExec> {
        setArgsString(File(generateTargetDir, "src/main/kotlin").absolutePath)
    }
    val setupTask by register("setupBuildGenerated") {
        group = "generation"
        doLast {
            BuildFileGenerator.generateBuildFiles(generateTargetDir, projectDir)
        }
    }
    val onlyBuildTask by register("buildGenerated") {
        group = "generation"
        mustRunAfter(":run")
        dependsOn(setupTask)
        doLast {
            BuildFileGenerator.buildGeneratedCode(generateTargetDir)
        }
    }
    register("generateAndBuild") {
        group = "generation"
        dependsOn(":run")
        dependsOn(onlyBuildTask)
        doLast {
            BuildFileGenerator.buildGeneratedCode(generateTargetDir)
        }
    }
    register("runGeneratedCode") {
        group = "generation"
        dependsOn(":run", setupTask)
        doLast {
            BuildFileGenerator.runGeneratedCode(generateTargetDir)
        }
    }
}

application {
    mainClassName = "dev.toliner.example.codegen.GreeterKt"
}

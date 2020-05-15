package dev.toliner.example.codegen

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import java.io.File

fun main(args: Array<String>) {
    val targetDir = args[0]
    val fileSpec = FileSpec.builder("dev.toliner", "HelloWorld")
        .addFunction(
            FunSpec.builder("main")
                .addCode("""println("Hello, World!)""")
                .build()
        )
        .build()
    fileSpec.writeTo(File(targetDir).apply { mkdirs() })
}

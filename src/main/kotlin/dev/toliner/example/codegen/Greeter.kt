package dev.toliner.example.codegen

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File

fun main(args: Array<String>) {
    val fileSpec = FileSpec.builder("dev.toliner", "Greeter")
        .addType(
            TypeSpec.classBuilder("Greeter")
                .addProperty(
                    PropertySpec.builder("name", String::class, KModifier.PRIVATE)
                        .initializer("name")
                        .build()
                )
                .primaryConstructor(
                    FunSpec.constructorBuilder()
                        .addParameter("name", String::class)
                        .build()
                )
                .addFunction(
                    FunSpec.builder("greet")
                        .addStatement("println(\"Hello, \$name\")")
                        .build()
                )
                .build()
        )
        .addFunction(
            FunSpec.builder("asGreeter")
                .receiver(String::class)
                .returns(ClassName("dev.toliner", "Greeter"))
                .addStatement("return Greeter(this)")
                .build()
        )
        .addFunction(
            FunSpec.builder("main")
                .addParameter("args", Array<String>::class.parameterizedBy(String::class))
                .addStatement("args[0].asGreeter().greet()")
                .build()
        )
        .build()


    val targetDir = args[0]
    fileSpec.writeTo(File(targetDir).apply { mkdirs() })
}
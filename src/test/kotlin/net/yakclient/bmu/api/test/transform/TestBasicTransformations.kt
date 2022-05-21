package net.yakclient.bmu.api.test.transform

import net.yakclient.bmu.api.Archives
import net.yakclient.bmu.api.transform.AwareClassWriter
import net.yakclient.bmu.api.transform.Sources
import net.yakclient.bmu.api.transform.TransformerConfig
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import kotlin.test.Test

class TestBasicTransformations {
    @Test
    fun `Test replace methods`() {
        val config = TransformerConfig.of {
            transformMethod("testMethod()V") {
                it.instructions = Sources.sourceOf(::methodToInject).get()

                it
            }
        }

        val new = Archives.resolve(ClassReader(ClassToTransform::class.java.name), config)

        val loader = object : ClassLoader() {
            fun defineClass(name: String, bytes: ByteArray) = super.defineClass(name, bytes, 0, bytes.size)
        }

        val c = loader.defineClass(ClassToTransform::class.java.name, new)
        val instance = c.getConstructor().newInstance()
        c.getMethod("testMethod").invoke(instance)
    }

    @Test
    fun `Test aware classwriter`() {
        val archive = Archives.find(
            File(this::class.java.getResource("/resources/archive-mapper-TEST-JAR.jar")!!.toURI()).toPath(),
            Archives.zipFinder
        )

        val writer = AwareClassWriter(archive, 0)
        assert(writer.getCommonSuperClass("java/lang/String", "java/lang/String") == "java/lang/Object")
        assert(
            writer.getCommonSuperClass(
                "net/yakclient/archive/mapper/parsers/DefaultParserProvider",
                "net/yakclient/archive/mapper/ParserProvider"
            ) == "net/yakclient/archive/mapper/ParserProvider"
        )
    }

    @Test
    fun `Test aware class writer without modifications`() {
        val archive = Archives.find(
            File(this::class.java.getResource("/resources/archive-mapper-TEST-JAR.jar")!!.toURI()).toPath(),
            Archives.zipFinder
        )

        val config = TransformerConfig.of { }

       Archives.resolve(
            ClassReader(archive.reader["net/yakclient/archive/mapper/ObfuscationMap.class"]!!.resource.open()),
            config,
            AwareClassWriter(archive, ClassWriter.COMPUTE_FRAMES)
        )
    }

    fun methodToInject() {
        println("Hello")

        println("._.")

        println("Not world")
    }
}

class ClassToTransform {
    fun testMethod() {
        println("Hello")

        println("...")

        println("World?")
    }
}
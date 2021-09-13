@file:JvmName("MethodNodeKt")

package net.yakclient.mixins.base.extension

import net.yakclient.mixins.base.extension.SearchType.*
import net.yakclient.mixins.base.ByteCodeUtils
import org.objectweb.asm.tree.MethodNode


public fun MethodNode.parameters(): List<Class<*>> = compiledDescriptionOf(this.desc)

public fun compiledDescriptionOf(desc: String): List<Class<*>> = listOf {
    fun <E : Enum<E>> E.or(vararg type: Enum<E>): Boolean = type.any { equals(it) }
    fun StringBuilder.containedClass(): Class<*> =
        Class.forName(this.toString().replace('/', '.')).also { this.clear() }

    val builder = StringBuilder()
    var type = NONE

    for (c in desc.trim('(', ')')) {
        if (c == 'L' && type == NONE) type = OBJECT
        else if (c == 'L' && type == ARRAY_NOT_DETERMINED) {
            type = ARRAY_OBJECT
            builder.append(c)
        } else if (c == '[' && type.or(NONE, ARRAY_NOT_DETERMINED)) {
            type = ARRAY_NOT_DETERMINED
            builder.append(c)
        } else if (c == ';' && type.or(OBJECT, ARRAY_OBJECT)) {
            if (type == ARRAY_OBJECT) builder.append(c)
            type = NONE
            add(builder.containedClass())
        } else {
            val primitiveType = ByteCodeUtils.isPrimitiveType(c)
            if (primitiveType && type == ARRAY_NOT_DETERMINED) {
                type = NONE
                builder.append(c)
                add(builder.containedClass())
            } else if (primitiveType && type == NONE) add(
                checkNotNull(
                    ByteCodeUtils.primitiveType(c)
                ) { "Failed primitive type" })
            else builder.append(c)
        }
    }
}

public fun MethodNode.sameSignature(signature: String): Boolean =
    ByteCodeUtils.sameSignature(name + desc, signature)

public fun MethodNode.sameSignature(signature: ByteCodeUtils.MethodSignature): Boolean =
    ByteCodeUtils.MethodSignature.of(name + desc).matches(signature)

private fun <T> listOf(builder: MutableList<T>.() -> Unit) : List<T> = ArrayList<T>().apply(builder).toList()

private enum class SearchType {
    OBJECT,
    ARRAY_OBJECT,
    ARRAY_NOT_DETERMINED,
    NONE
}

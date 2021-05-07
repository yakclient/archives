package net.yakclient.mixin.base.internal.instruction

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils
import org.objectweb.asm.tree.MethodNode

open class DirectInsnInterceptor : MethodNode(ByteCodeUtils.ASM_VERSION), InstructionInterceptor {
    override fun intercept(): Instruction {
        return DirectInstruction(instructions)
    }
}
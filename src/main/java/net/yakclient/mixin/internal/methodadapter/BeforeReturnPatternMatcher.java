package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;

public class BeforeReturnPatternMatcher extends MethodInjectionPatternMatcher {
    public BeforeReturnPatternMatcher(MethodVisitor visitor, Instruction instruction) {
        super(visitor, instruction);
    }

    @Override
    public void visitInsn(int opcode) {
        if (this.isReturn(opcode)) this.executeInsn();
        super.visitInsn(opcode);
    }

    @Override
    public void visitInsn() {
    }
}
package net.yakclient.mixin.base.internal.instruction.adapter

import net.yakclient.mixin.base.internal.instruction.Instruction

open class InsnAdapter {
    private val adapter: InsnAdapter?

    constructor(adapter: InsnAdapter?) {
        this.adapter = adapter
    }

    constructor() {
        adapter = null
    }

    open fun adapt(instruction: Instruction): Instruction {
        return adapter?.adapt(instruction) ?: instruction
    }
}
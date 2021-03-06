package me.sizableshrimp.adventofcode2020.days;

import me.sizableshrimp.adventofcode2020.intcode.Instruction;
import me.sizableshrimp.adventofcode2020.intcode.Intcode;
import me.sizableshrimp.adventofcode2020.intcode.OpCode;
import me.sizableshrimp.adventofcode2020.templates.Day;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Day08 extends Day {
    private List<Instruction> baseInstructions;

    @Override
    protected Result evaluate() {
        Intcode.ExitLoop exitLoop = new Intcode(baseInstructions).runUntilRepeat();
        long part1 = exitLoop.getAccumulator();

        Set<Instruction> possible = StreamEx.of(exitLoop.getSeen())
                .filter(inst -> inst.getCode() == OpCode.JUMP || inst.getCode() == OpCode.NOP)
                .toSet();

        for (Instruction inst : possible) {
            Instruction opposite = switch (inst.getCode()) {
                case JUMP -> inst.changeCode(OpCode.NOP);
                case NOP -> inst.changeCode(OpCode.JUMP);
                default -> throw new IllegalStateException("wrong");
            };
            List<Instruction> copy = new ArrayList<>(baseInstructions);
            copy.set(opposite.getIndex(), opposite);
            Intcode.ExitState exitState = new Intcode(copy).runUntilExitOrRepeat();
            if (exitState.isExited()) {
                return Result.of(part1, exitState.getAccumulator());
            }
        }

        throw new IllegalStateException();
    }

    @Override
    protected void parse() {
        baseInstructions = Instruction.parseLines(lines);
    }
}

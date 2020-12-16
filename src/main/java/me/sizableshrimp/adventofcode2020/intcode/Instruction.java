/*
 * AdventOfCode2020
 * Copyright (C) 2020 SizableShrimp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.sizableshrimp.adventofcode2020.intcode;

import lombok.Value;
import me.sizableshrimp.adventofcode2020.helper.MatchWrapper;
import me.sizableshrimp.adventofcode2020.helper.Parser;

import java.util.ArrayList;
import java.util.List;

@Value
public class Instruction {
    int index;
    OpCode code;
    int[] args;

    public static List<Instruction> parseLines(List<String> lines) {
        List<Instruction> instructions = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            instructions.add(Instruction.parse(i, line));
        }

        return instructions;
    }

    public static Instruction parse(int index, String line) {
        MatchWrapper match = Parser.parseMatch("(\\w+?) (.+?)", line);

        OpCode code = OpCode.getOpCode(match.group(1));

        String[] split = match.group(2).split(" ");
        int[] args = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            String arg = split[i];
            args[i] = Integer.parseInt(arg);
        }

        return new Instruction(index, code, args);
    }

    /**
     * Returns a copy of the current {@link Instruction} with a modified {@link OpCode}.
     *
     * @param newCode The new {@link OpCode} to use.
     * @return A copy of the current instance with a modified {@link OpCode}.
     */
    public Instruction changeCode(OpCode newCode) {
        return new Instruction(index, newCode, args);
    }
}

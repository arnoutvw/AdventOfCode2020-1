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

package me.sizableshrimp.adventofcode2020.helper;

import me.sizableshrimp.adventofcode2020.templates.Coordinate;
import me.sizableshrimp.adventofcode2020.templates.EnumState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static final Pattern pattern = Pattern.compile("(\\d+,\\d+)");
    private static final HashMap<String, Pattern> CACHED_PATTERNS = new HashMap<>();

    /**
     * Attempts to find every coordinate in the provided line and returns them in a list, in order.
     *
     * @param line The provided line to search for coordinates.
     * @return A list of {@link Coordinate}s found in the provided line, in order.
     */
    public static List<Coordinate> parseCoordinates(String line) {
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            coordinates.add(Coordinate.parse(matcher.group(1)));
        }

        return coordinates;
    }

    /**
     * Parses each line from the input list using the provided pattern and {@link Matcher#matches()}.
     *
     * @param pattern The pattern to use for matching to each line.
     * @param lines The list of lines to parse.
     * @param consumer The consumer to accept the {@link MatchWrapper} for each line.
     * Use {@link MatchResult#group()} to get the full line.
     */
    public static void parseLines(Pattern pattern, List<String> lines, Consumer<MatchWrapper> consumer) {
        for (String line : lines) {
            consumer.accept(parseMatch(pattern, line));
        }
    }

    /**
     * Creates a {@link Matcher} from the provided {@link Pattern} and {@link String} input.
     * The returned value will be a {@link MatchWrapper} that wraps a {@link MatchResult}
     * with overloaded helper functions.
     *
     * @param pattern The {@link Pattern} used to match the provided input.
     * @param input The {@link String} input to be matched against the pattern.
     * @return A {@link MatchWrapper} that wraps a {@link MatchResult}
     */
    public static MatchWrapper parseMatch(Pattern pattern, String input) {
        return parseMatch(pattern.matcher(input));
    }

    public static MatchWrapper parseMatch(Matcher matcher) {
        if (!matcher.matches())
            throw new IllegalStateException("Matcher does not match the full input string");
        return new MatchWrapper(matcher.toMatchResult());
    }

    /**
     * Parses each line from the input list using the provided pattern and {@link Matcher#find}.
     *
     * @param pattern The pattern to use for matching to each line.
     * @param lines The list of lines to parse.
     * @param consumer The consumer to use the matcher and line data returned for each line.
     */
    public static void parseLinesFind(Pattern pattern, List<String> lines, BiConsumer<Matcher, String> consumer) {
        for (String line : lines) {
            Matcher m = pattern.matcher(line);
            m.find();
            consumer.accept(m, line);
        }
    }

    public static <T extends Enum<T> & EnumState<T>> T parseEnumState(T[] enumConstants, char c) {
        for (T t : enumConstants) {
            if (t.getMappedChar() == c)
                return t;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Creates a {@link Matcher} from the provided regex {@link String} and {@link String} input.
     * The regex string is compiled to a {@link Pattern} and cached for optimization purposes.
     * The returned value will be a {@link MatchWrapper} that wraps a {@link MatchResult}
     * with overloaded helper functions.
     *
     * @param regex The regex {@link String} used to match the provided input.
     * This string is compiled to a {@link Pattern} and cached for optimization purposes.
     * @param input The {@link String} input to be matched against the pattern.
     * @return A {@link MatchWrapper} that wraps a {@link MatchResult}
     */
    public static MatchWrapper parseMatch(String regex, String input) {
        Pattern pattern = CACHED_PATTERNS.computeIfAbsent(regex, Pattern::compile);
        return parseMatch(pattern.matcher(input));
    }
}

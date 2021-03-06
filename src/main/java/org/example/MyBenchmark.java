/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.example;

import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class MyBenchmark {

    @Param({"2", "5", "8"})
    public int substringLength;

    @Param({"10", "100", "1000"})
    public int wordLength;

    public String first = "";
    public String second = "";


    @Setup(Level.Trial)
    public void setupWords() {
        StringBuilder firstB = new StringBuilder();
        StringBuilder secondB = new StringBuilder();
        for (int i = 0; i < (wordLength * substringLength) / 10; i++) {
            firstB.append("a");
            secondB.append("a");
        }
        for (int i = (wordLength * substringLength) / 10; i < wordLength; i++) {
            firstB.append("1");
            secondB.append("2");
        }

        first = firstB.toString();
        second = secondB.toString();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .forks(1)
                .build();

        new Runner(opt).run();
    }

    /**
     * ???????????????????? ?????????? ??????????????????.
     *
     * ???????? ?????? ????????????, ???????????????? ???????????????????????? ?? ????????????????????????.
     * ?????????? ???? ?????????? ?????????????? ?????????? ?????????????????? -- ?? ?????????????? ?????? ????????????????.
     * ???????? ?????????? ???????????????? ??????, ?????????????? ???????????? ????????????.
     * ?????? ?????????????????? ????????????????, ?????????????? ???????????????? *??????????* ????????????????.
     * ???????? ?????????????? ?????????????????? ?????????? ?????????????? ?????????? ???????????????? ?????????? ??????????,
     * ?????????????? ???? ???? ??????, ?????????????? ?????????????????????? ???????????? ?? ???????????? first.
     *
     * ?????? ???????????? ?????????????????? ?????????????? ?????????? ?????????? ???????? ???????????????????? ?????????????????? ???? ???????????? ?????????? ?????????????? ????????????????
     */
    @Benchmark
    public String longestCommonSubstringOne() {
        //?????????????????? O(first*second*min(first, second))
        //???????????? O(1)

        if (first == null || second == null || first.length() == 0 || second.length() == 0) return "";

        if (first.equals(second)) return first;

        int resultLength = 0;
        int index = 0;

        for (int i = 0; i < first.length(); i++) {

            for (int j = 0; j < second.length(); j++) {

                int k = 0;

                while (i + k < first.length() && j + k < second.length() && first.charAt(i + k) == second.charAt(j + k))
                    k = k + 1;

                if (k > resultLength) {
                    resultLength = k;
                    index = i;
                }
            }
        }
        return first.substring(index, index + resultLength);
    }

    /**
     * ???????????????????? ?????????? ??????????????????.
     *
     * ?????????????????? ?????????????????? ????????????, ?? ?????????????? ???????????????????? ?????????? ?? ???????????????? ???????????????????????? ?????????????? ???????? ???????????? ????????.
     * ?????????? ?????????????? ?????????????????? ?????????????? ?????????????????????? - ???????? ???????????????????? ?? ???????????? ??????, ?????? ?????????? ????????, ???????? ???????? -
     * ?????? ???????????????????????? ??????????, ???????????? ?????????? ?????????? ??????????????????, ?? ?????? ?????????? ???????????? ???????????????? ???????????????????? ???????????????? ??
     * ?????????????? ???? ??????????????????.
     */
    @Benchmark
    public String longestCommonSubstringTwo() {
        //?????????????????? O(firs*second)
        //???????????? O(firs*second)

        if (first == null || second == null || first.length() == 0 || second.length() == 0) return "";

        if (first.equals(second)) return first;

        int[][] matrix = new int[first.length()][];

        int maxLength = 0;
        int maxIndex = 0;

        for (int i = 0; i < matrix.length; i++) {

            matrix[i] = new int[second.length()];

            for (int j = 0; j < matrix[i].length; j++) {
                if (first.charAt(i) == second.charAt(j)) {

                    if (i != 0 && j != 0) matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    else matrix[i][j] = 1;

                    if (matrix[i][j] > maxLength) {
                        maxLength = matrix[i][j];
                        maxIndex = i;
                    }
                }
            }
        }
        return first.substring(maxIndex - maxLength + 1, maxIndex + 1);
    }

}

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

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;


public class MyBenchmark {

    @Benchmark @Fork(1) @BenchmarkMode(Mode.AverageTime) @OutputTimeUnit(TimeUnit.MICROSECONDS)

    public void testMethod() {
        String first = "abcde";
        String second = "fgbcd";

        //longestCommonSubstringOne(first, second);
        longestCommonSubstringTwo(first, second);
    }


    /**
     * Наибольшая общая подстрока.
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     *
     * Для каждой подстроки первого слова метод ищет идентичные подстроки во втором слове методом перебора
     */

    static public String longestCommonSubstringOne(String first, String second) {

        //сложность O(first*second*min(first, second))
        //память O(1)

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
     * Наибольшая общая подстрока.
     *
     * Создается двумерный массив, в котором количество строк и столбцов определяется длинами двух данных слов.
     * Таким образом создается таблица пересечений - если совпадений в словах нет, там стоит ноль, если есть -
     * там записывается число, равное длине общей подстроки, а его можно узнать проверив предыдущее значение в
     * таблице по диагонали.
     */

    static public String longestCommonSubstringTwo(String firs, String second) {

        //сложность O(firs*second)
        //память O(firs*second)

        if (firs == null || second == null || firs.length() == 0 || second.length() == 0) return "";

        if (firs.equals(second)) return firs;

        int[][] matrix = new int[firs.length()][];

        int maxLength = 0;
        int maxIndex = 0;

        for (int i = 0; i < matrix.length; i++) {

            matrix[i] = new int[second.length()];

            for (int j = 0; j < matrix[i].length; j++) {
                if (firs.charAt(i) == second.charAt(j)) {

                    if (i != 0 && j != 0) matrix[i][j] = matrix[i - 1][j - 1] + 1;
                    else matrix[i][j] = 1;

                    if (matrix[i][j] > maxLength) {
                        maxLength = matrix[i][j];
                        maxIndex = i;
                    }
                }
            }
        }
        return firs.substring(maxIndex - maxLength + 1, maxIndex + 1);
    }

}

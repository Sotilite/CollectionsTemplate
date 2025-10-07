package ru.naumen.collection.task3;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>Написать консольное приложение, которое принимает на вход произвольный текстовый файл в формате txt.
 * Нужно собрать все встречающийся слова и посчитать для каждого из них количество раз, сколько слово встретилось.
 * Морфологию не учитываем.</p>
 * <p>Вывести на экран наиболее используемые (TOP) 10 слов и наименее используемые (LAST) 10 слов</p>
 * <p>Проверить работу на романе Льва Толстого “Война и мир”</p>
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class WarAndPeace
{

    private static final Path WAR_AND_PEACE_FILE_PATH = Path.of("src/main/resources",
            "Лев_Толстой_Война_и_мир_Том_1,_2,_3,_4_(UTF-8).txt");

    /**
     * Выбрал HashMap, поскольку операции добавления и получения элемента выполняются за O(1).
     * Такая алгоритмическая сложность гарантируется встроенной реализацией функций equals, hashCode.
     * Общая сложность алгоритма O(n + m log(m)), где O(n) - чтение файла и подсчет слов, а O(m log(m)) -
     * встроенная сортировка TimSort, основанная на сортировке вставками и слиянием, где m - количество
     * уникальных слов. Операции .limit и .skip выполняются за константное время.
     */
    public static void main(String[] args) {
        Map<String, Integer> wordCountMap = new HashMap<>();
        new WordParser(WAR_AND_PEACE_FILE_PATH)
                .forEachWord(word -> {
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                });

        var sortedWords = new ArrayList<>(wordCountMap.entrySet())
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .toList();

        var top10Words = sortedWords.stream()
                .limit(10)
                .toList();

        var last10Words = sortedWords.stream()
                .skip(sortedWords.size() - 10)
                .toList();

        System.out.println("TOP 10 слов:");
        top10Words.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\nLAST 10 слов:");
        last10Words.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}

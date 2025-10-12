package ru.naumen.collection.task3;

import java.nio.file.Path;
import java.util.*;

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
     * Выбрал LinkedHashMap для подсчета количества слов, поскольку итерируется быстрее чем HashMap.
     * Для отбора TOP и LAST 10 использую PriorityQueue с уже заданным размером (небольшая оптимизация)
     * Первая куча будет в порядке возрастания, для того, чтобы эффективно удалять наименьший элемент, а
     * вторая в порядке убывания - эффективно удалять наибольший элемент. В одном цикле проходимся по словарю
     * и провереям, если размер меньше 10 - добавлеям, иначе, если добавляемый элемент больше, чем наим.
     * в TOP 10, тогда первый добавляем, второй удаляем соответственно (То же самое с LAST 10, только элемент
     * добавиться, если он меньше, чем наиб. в LAST 10). В других случаях не имеет смысла добавлять элементы.
     * Так как мне нужно вывести TOP 10 в порядке убывания, то очередь нужно развернуть. Встроенной реализации
     * для этого нет, поэтому использую список, разворачиваю порядок и вывожу. Для LAST 10 порядок не важен,
     * так как все слова встречаются всего лишь по разу, поэтому просто вывожу.
     * Общая сложность - O(n + m log k), где n - количество слов в романе, m - количество уникальных слов,
     * k = 10, количество слов в куче. Почему m log k? Потому что функции poll и offer имеют логарифмическую
     * сложность в Priority Queue. LinkedHashMap гарантирует операцию вставки за O(1) за счет внутренней
     * реализации в самом языке Java.
     */
    public static void main(String[] args) {
        Map<String, Integer> wordCountMap = new LinkedHashMap<>();
        new WordParser(WAR_AND_PEACE_FILE_PATH)
                .forEachWord(word -> {
                    wordCountMap.merge(word, 1, Integer::sum);
                });

        int limit = 10;
        PriorityQueue<Map.Entry<String, Integer>> top10Words = new PriorityQueue<>(
                limit + 1, Comparator.comparingInt(Map.Entry::getValue)
        );
        PriorityQueue<Map.Entry<String, Integer>> last10Words = new PriorityQueue<>(
                limit + 1, (e1, e2) -> e2.getValue().compareTo(e1.getValue())
        );

        for (Map.Entry<String, Integer> entry : wordCountMap.entrySet()) {
            if(top10Words.size() < limit) {
                top10Words.offer(entry);
            } else {
                if (entry.getValue() > top10Words.peek().getValue()) {
                    top10Words.poll();
                    top10Words.offer(entry);
                }
            }

            if(last10Words.size() < limit) {
                last10Words.offer(entry);
            } else {
                if (entry.getValue() < last10Words.peek().getValue()) {
                    last10Words.poll();
                    last10Words.offer(entry);
                }
            }
        }

        List<Map.Entry<String, Integer>> topWords = new ArrayList<>();
        while (!top10Words.isEmpty()) {
            topWords.add(top10Words.poll());
        }
        Collections.reverse(topWords);
        topWords.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

        System.out.println("\nLAST 10 слов:");
        last10Words.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }
}

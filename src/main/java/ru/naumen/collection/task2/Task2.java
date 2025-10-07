package ru.naumen.collection.task2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Дано:
 * <pre>
 * public class User {
 *     private String username;
 *     private String email;
 *     private byte[] passwordHash;
 *     …
 * }
 * </pre>
 * Нужно реализовать метод
 * <pre>
 * public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB);
 * </pre>
 * <p>который возвращает дубликаты пользователей, которые есть в обеих коллекциях.</p>
 * <p>Одинаковыми считаем пользователей, у которых совпадают все 3 поля: username,
 * email, passwordHash. Дубликаты внутри коллекций collA, collB можно не учитывать.</p>
 * <p>Метод должен быть оптимален по производительности.</p>
 * <p>Пользоваться можно только стандартными классами Java SE.
 * Коллекции collA, collB изменять запрещено.</p>
 *
 * См. {@link User}
 *
 * @author vpyzhyanov
 * @since 19.10.2023
 */
public class Task2
{

    /**
     * Возвращает дубликаты пользователей, которые есть в обеих коллекциях.
     * Выбрал HashSet, поскольку эффективно проверяет принадлежность элемента к коллекции за O(1).
     * Алгоритмическая сложность O(n + m), где n - количество операций добавления элементов в
     * setA за O(1) для каждой операции, m - количество операций поиска элементов из collB в
     * HashSet за O(1) для каждой операции. Такая алгоритмическая сложность гарантируется правильным
     * переопределением методов equals и hashCode() с использованием встроенной функции hash класса Objects.
     */
    public static List<User> findDuplicates(Collection<User> collA, Collection<User> collB) {
        if(collA == null || collB == null) {
            return  new ArrayList<>();
        }

        var setA = new HashSet<>(collA);

        return collB.stream()
                .filter(setA::contains)
                .toList();
    }
}

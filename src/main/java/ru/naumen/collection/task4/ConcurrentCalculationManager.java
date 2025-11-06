package ru.naumen.collection.task4;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * Класс управления расчётами.
 * Выбрал потокобезопасную очередь LinkedBlockingQueue, потому что она гарантирует
 * FIFO порядок, динамически расширяется, благодаря блокирующим операциям put() и take(),
 * не приведет к ошибке или получению null, если очередь пуста.
 * За счет реализации очереди на основе двусвязного списка сложность операций put() и take()
 * гарантирована за O(1). Общая алгоритмическая сложность O(n), где n - количество задач.
 */
public class ConcurrentCalculationManager<T> {
    //Небольшая оптимизация, если знаем заранее размер очереди.
    private final BlockingQueue<Future<T>> resultQueue = new LinkedBlockingQueue<>(3);

    /**
     * Добавить задачу на параллельное вычисление
     */
    public void addTask(Supplier<T> task) {
        try {
            Future<T> future = CompletableFuture.supplyAsync(task);
            resultQueue.put(future);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Получить результат вычисления.
     * Возвращает результаты в том порядке, в котором добавлялись задачи.
     */
    public T getResult() {
        try {
            return resultQueue.take().get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
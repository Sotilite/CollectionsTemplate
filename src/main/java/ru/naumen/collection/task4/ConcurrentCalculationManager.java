package ru.naumen.collection.task4;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

/**
 * Класс управления расчётами.
 * Выбрал потокобезопасную очередь LinkedBlockingQueue, потому что она гарантирует
 * FIFO порядок, динамически расширяется, благодаря блокирующим операциям put() и take() не
 * приведет к ошибке или получению null, если очередь пуста.
 * За счет реализации очереди на основе двусвязного списка сложность операций put() и take()
 * гарантирована за O(1). Общая алгоритмическая сложность O(n * m), где n - количество задач,
 * m - максимальное кол-во опережающих задач (т. е. завершилась раньше предыдущих и должна
 * ожидать их выполнения - в нашем случае - цикл while)
 */
public class ConcurrentCalculationManager<T> {
    private final BlockingQueue<T> resultQueue = new LinkedBlockingQueue<>();
    private final AtomicInteger taskCounter = new AtomicInteger(0);
    private final AtomicInteger resultCounter = new AtomicInteger(0);

    /**
     * Добавить задачу на параллельное вычисление
     */
    public void addTask(Supplier<T> task) {
        var currentTaskNumber = taskCounter.incrementAndGet();

        new Thread(() -> {
            T result = task.get();

            // Ожидание своей очереди в порядке добавления
            while (resultCounter.get() + 1 != currentTaskNumber) {
                Thread.onSpinWait();
            }

            try {
                resultQueue.put(result);
                resultCounter.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    /**
     * Получить результат вычисления.
     * Возвращает результаты в том порядке, в котором добавлялись задачи.
     */
    public T getResult() {
        try {
            return resultQueue.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Поток был прерван", e);
        }
    }
}
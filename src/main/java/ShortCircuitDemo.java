import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ShortCircuitDemo {

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        AtomicInteger counter = new AtomicInteger();

        Predicate<Integer> isEven = number -> {
            counter.incrementAndGet();
            return number % 2 == 0;
        };

        boolean result = numbers.stream()
                .anyMatch(isEven);

        System.out.println("Результат anyMatch: " + result);
        System.out.println("Количество вызовов Predicate: " + counter.get());
    }
}

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class CollectionFunctions {

    private CollectionFunctions() {
    }

    public static <T> List<T> filter(
            List<T> source,
            Predicate<? super T> condition) {
        return source.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    public static <T, R> List<R> map(
            List<T> source,
            Function<? super T, ? extends R> mapper) {
        return source.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    public static <T> void forEach(
            List<T> source,
            Consumer<? super T> action) {
        source.stream().forEach(action);
    }
}

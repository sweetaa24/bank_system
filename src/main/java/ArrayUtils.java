public class ArrayUtils {

    public static <T> T first(T[] values) {
        return values[0];
    }

    public static <T> T last(T[] values) {
        return values[values.length - 1];
    }

    public static <T> boolean contains(T[] values, T target) {
        for (T value : values) {
            if (value.equals(target)) {
                return true;
            }
        }
        return false;
    }
}
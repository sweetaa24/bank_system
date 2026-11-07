import java.util.List;

public class ReduceDemo {

    public static void main(String[] args) {
        List<Double> balances = List.of(150000.0, 200000.0, 75000.0);

        // 1. Вычисление суммы через цикл
        double totalFor = 0;

        for (Double balance : balances) {
            totalFor += balance;
        }

        System.out.println("Сумма через for: " + totalFor);

        // 2. Вычисление суммы через reduce
        double totalReduce = balances.stream()
                .reduce(0.0, Double::sum);

        System.out.println("Сумма через reduce: " + totalReduce);
    }
}
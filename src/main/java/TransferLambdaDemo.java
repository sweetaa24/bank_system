
public class TransferLambdaDemo {

    public static void main(String[] args) {
        CommissionPolicy noCommission = amount -> 0.0;

        CommissionPolicy onePercent =
                amount -> amount * 0.01;

        System.out.println("Комиссия без комиссии: "
                + noCommission.calculate(1000));

        System.out.println("Комиссия 1%: "
                + onePercent.calculate(1000));

        NotificationService consoleLambda =
                message -> System.out.println(message);

        consoleLambda.notify("Уведомление через lambda");

        NotificationService console = System.out::println;

        console.notify("Уведомление через method reference");
    }
}

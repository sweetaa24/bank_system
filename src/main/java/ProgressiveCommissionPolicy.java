public class ProgressiveCommissionPolicy implements CommissionPolicy {

    private final double threshold;
    private final double firstPercent;
    private final double secondPercent;

    public ProgressiveCommissionPolicy(
            double threshold,
            double firstPercent,
            double secondPercent) {

        if (threshold < 0 || firstPercent < 0 || secondPercent < 0) {
            throw new IllegalArgumentException(
                    "Commission parameters cannot be negative"
            );
        }

        this.threshold = threshold;
        this.firstPercent = firstPercent;
        this.secondPercent = secondPercent;
    }

    @Override
    public double calculate(double amount) {
        if (amount <= threshold) {
            return amount * firstPercent / 100;
        }

        return threshold * firstPercent / 100
                + (amount - threshold) * secondPercent / 100;
    }
}

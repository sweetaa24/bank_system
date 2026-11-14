package bank.domain;

public class FixedCommissionPolicy implements CommissionPolicy {

    private final double fixedAmount;

    public FixedCommissionPolicy(double fixedAmount) {
        if (fixedAmount < 0) {
            throw new IllegalArgumentException(
                    "Commission cannot be negative"
            );
        }

        this.fixedAmount = fixedAmount;
    }

    @Override
    public double calculate(double amount) {
        return fixedAmount;
    }
}

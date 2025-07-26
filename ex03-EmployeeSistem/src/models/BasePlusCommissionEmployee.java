package models;

import java.text.NumberFormat;

// Classe BasePlusCommissionEmployee estende a CommissionEmployee.
public class BasePlusCommissionEmployee extends CommissionEmployee {
    private double baseSalary;

    public BasePlusCommissionEmployee(String firstName, String lastName,
                                      String socialSecurityNumber, double grossSales,
                                      double commissionRate, double baseSalary) {
        super(firstName, lastName, socialSecurityNumber,
                grossSales, commissionRate);
        if (baseSalary < 0.0)
            throw new IllegalArgumentException("Base salary must be >= 0.0");
        this.baseSalary = baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        if (baseSalary < 0.0)
            throw new IllegalArgumentException("Base salary must be >= 0.0");
        this.baseSalary = baseSalary;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    @Override
    public double getPaymentAmount() {
        return getBaseSalary() + super.getPaymentAmount();
    }

    @Override
    public String toString() {
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        return String.format("""
                base-salaried commission employee: %s %s
                social security number: %s
                base salary: %s
                gross sales: %s
                commission rate: %s""",
                getFirstName(), getLastName(),
                getSocialSecurityNumber(),
                fmt.format(getBaseSalary()),
                fmt.format(getGrossSales()),
                NumberFormat.getPercentInstance().format(getCommissionRate()));
    }
}

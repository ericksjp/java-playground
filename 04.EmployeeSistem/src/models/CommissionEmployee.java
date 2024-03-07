package models;

import java.text.NumberFormat;

// Figura 10.7: CommissionEmployee.java
// Classe CommissionEmployee estende Employee.
public class CommissionEmployee extends Employee
{
    private double grossSales; // vendas brutas semanais
    private double commissionRate; // porcentagem da comissão

    public CommissionEmployee(String firstName, String lastName,
                              String socialSecurityNumber, double grossSales,
                              double commissionRate)
    {
        super(firstName, lastName, socialSecurityNumber);
        if (commissionRate <= 0.0 || commissionRate >= 1.0) // valida
            throw new IllegalArgumentException("Commission rate must be > 0.0 and < 1.0");
        if (grossSales < 0.0) // valida
            throw new IllegalArgumentException("Gross sales must be >= 0.0");
        this.grossSales = grossSales;
        this.commissionRate = commissionRate;
    }

    public void setGrossSales(double grossSales)
    {
        if (grossSales < 0.0) // valida
            throw new IllegalArgumentException("Gross sales must be >= 0.0");
        this.grossSales = grossSales;
    }

    public double getGrossSales()
    {
        return grossSales;
    }

    public void setCommissionRate(double commissionRate)
    {
        if (commissionRate <= 0.0 || commissionRate >= 1.0) // valida
            throw new IllegalArgumentException("Commission rate must be > 0.0 and < 1.0");
        this.commissionRate = commissionRate;
    }

    public double getCommissionRate()
    {
        return commissionRate;
    }

    @Override
    public double getPaymentAmount()
    {
        return getCommissionRate() * getGrossSales();
    }

    @Override
    public String toString()
    {
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        return String.format("""
                commission employee: %s
                gross sales: %s
                commission rate: %s""", super.toString(), fmt.format(getGrossSales()), NumberFormat.getPercentInstance().format(getCommissionRate()));
    }
}
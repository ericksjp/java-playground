package models;

import java.text.NumberFormat;

// Figura 10.6: HourlyEmployee.java
// Classe HourlyEmployee estende Employee.
public class HourlyEmployee extends Employee
{
    private double wage; // salário por hora
    private double hours; // horas trabalhadas durante a semana

    public HourlyEmployee(String firstName, String lastName,
                          String socialSecurityNumber, double wage, double hours)
    {
        super(firstName, lastName, socialSecurityNumber);
        if (wage < 0.0) // valida remuneração
            throw new IllegalArgumentException(
                    "Hourly wage must be >= 0.0");
        if ((hours < 0.0) || (hours > 168.0)) // valida horas
            throw new IllegalArgumentException(
                    "Hours worked must be >= 0.0 and <= 168.0");
        this.wage = wage;
        this.hours = hours;
    }

    public void setWage(double wage)
    {
        if (wage < 0.0) // valida remuneração
            throw new IllegalArgumentException(
                    "Hourly wage must be >= 0.0");
        this.wage = wage;
    }

    public double getWage()
    {
        return wage;
    }

    public void setHours(double hours)
    {
        if ((hours < 0.0) || (hours > 168.0)) // valida horas
            throw new IllegalArgumentException(
                    "Hours worked must be >= 0.0 and <= 168.0");
        this.hours = hours;
    }

    public double getHours()
    {
        return hours;
    }

    @Override
    public double getPaymentAmount()
    {
        if (getHours() <= 40)
            return getWage() * getHours();
        else
            return 40 * getWage() + (getHours() - 40) * getWage() * 1.5;
    }

    @Override
    public String toString()
    {
        return String.format("""
                hourly employee: %s
                hourly wage: %s
                hours worked: %,.2f""",
                super.toString(), NumberFormat.getCurrencyInstance().format(getWage()), getHours());
    }
}
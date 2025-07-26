package views;

import interfaces.Payable;
import models.*;

public class Main {
    public static void main(String[] args) {
        Payable[] payableObjects = new Payable[7];

        payableObjects[0] = new Invoice("01234", "seat", 2, 375.00);
        payableObjects[1] = new Invoice("56789", "tire", 4, 79.95);
        payableObjects[2] = new SalariedEmployee("John", "Smith", "111-11-1111", 800.00);
        payableObjects[3] = new BasePlusCommissionEmployee("Lisa", "Barnes", "555-55-5555", 1200.00, 0.1, 300);
        payableObjects[4] = new HourlyEmployee("Karen", "Price", "222-22-2222", 16.75, 40);
        payableObjects[5] = new SalariedEmployee("Pedro", "Gonzalez", "333-33-3333", 1000.00);
        payableObjects[6] = new CommissionEmployee("Maria", "Martinez", "444-44-4444", 20.25, 0.5);
        System.out.println("Invoices and Employees processed polimorficamente:");
        for (Payable currentPayable : payableObjects) {
            if (currentPayable instanceof SalariedEmployee salariedEmployee) {
                salariedEmployee.setWeeklySalary(salariedEmployee.getWeeklySalary() * 1.10);
            }
            System.out.println(currentPayable);
            System.out.println();
        }
    }
}
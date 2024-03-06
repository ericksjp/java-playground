import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.

public class Main {
    public static void main(String[] args) {
        BigDecimal principal = BigDecimal.valueOf(1000.0);
        BigDecimal rate = BigDecimal.valueOf(0.05);
        System.out.printf("%s%20s%n", "Year", "Amount on deposit");
        // calcula quantidade de depósito para cada um dos dez anos
        for (int year = 1; year <= 10; year++)
        {
            BigDecimal amount = principal.multiply(rate.add(BigDecimal.ONE).pow(year));
            System.out.printf("%4d%20s%n", year, NumberFormat.getCurrencyInstance().format(amount));
        }

        BigDecimal result = principal.divide(BigDecimal.valueOf(3), MathContext.DECIMAL128);
        System.out.println("Divided value: " + result);
    }
}

//        var myDate = new MyDate(2023,5,28);
//        var date = LocalDate.of(2023,5,28);
//        int years = 70;
//        myDate.minusDays(years);
//        date = date.minusDays(years);
//        date = date.plusMonths(5);
//
//        System.out.println(myDate);
//        System.out.println(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        System.out.println(Integer.MAX_VALUE * 30L);
//
//        var book = Book.JHTP;
//        System.out.println(book.getTitle());
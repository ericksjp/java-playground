public class Main {
    public static void main(String[] args) {
        // Criando instância de MyDate
        MyDate date = new MyDate(2022, 3, 6);

        // Exibindo a data inicial
        System.out.println("Data Inicial: " + date);

        // Realizando operações de adição e subtração de anos, meses, semanas e dias
        date.plusYears(1);
        date.minusMonths(2);
        date.plusWeeks(3);
        date.minusDays(15);

        // Exibindo a data após as operações
        System.out.println("Data Após Operações: " + date);

        // Comparando datas
        MyDate otherDate = new MyDate(2022, 3, 6);
        int comparisonResult = MyDate.compareDate(date, otherDate);
        System.out.println("Resultado da Comparação: " + comparisonResult);

        // Exibindo a data formatada
        String formattedDate = date.toString(" - ", "dd - mm - yy");
        System.out.println("Data Formatada: " + formattedDate);
    }
}

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public abstract class GenerateLogs {
    private static final Random random = new Random();

    /*
     * Gera um arquivo de log com a quantidade de registros especificada
     */
    public static File generateLogFile(int quantidade, String filePath) throws IOException {
        File file = new File(filePath);

        BufferedWriter arquivo = new BufferedWriter(new FileWriter(file));

        for (int i = 0; i < quantidade; i++){
            String log = generateName() + " " + generateDate();
            arquivo.write(log + "\n");
        }
        arquivo.close();
        return file;
    }

    /*
     * Gera um nome aleatório para o usuário
     */
    private static String generateName(){
        String[] nomesArray = {
                "Alice", "Bob", "Carol", "David", "Eva", "Frank", "Grace", "Hannah", "Isaac", "Jane",
                "Kevin", "Linda", "Michael", "Nora", "Oliver", "Pamela", "Quincy", "Rachel", "Samuel",
                "Tina", "Ulysses", "Victoria", "Walter", "Xena", "Yvonne", "Zachary", "Sophia", "Liam",
                "Olivia", "Noah", "Emma", "Jackson", "Ava", "Lucas", "Mia", "Benjamin", "Charlotte",
                "Henry", "Amelia", "Ethan", "Harper", "Alexander", "Evelyn", "Sebastian", "Abigail",
                "Daniel", "Ella", "Matthew", "Scarlett", "Aiden", "Grace", "Mason", "Chloe", "Logan",
                "Victoria", "David", "Zoe", "Joseph", "Riley", "James", "Nora", "Elijah", "Avery",
                "William", "Addison", "Michael", "Lily", "Alexander", "Lillian", "Owen", "Natalie",
                "Jack", "Hannah", "Luke", "Aria", "Gabriel", "Layla", "Carter", "Brooklyn", "Jayden",
                "Alexa", "John", "Zoey", "Dylan", "Penelope", "Wyatt", "Savannah", "Isaiah", "Violet",
        };

        return nomesArray[random.nextInt(nomesArray.length)] + random.nextInt(101);
    }

    /*
     * Gera uma data aleatória no formato ISO 8601
     */
    private static String generateDate(){
        int[] monthDays = {31,28,31,30,31,30,31,31,30,31,30,31};
        var formatter = DateTimeFormatter.ISO_DATE_TIME;

        int year = random.nextInt(30) + 1990 + 1;
        int month = random.nextInt(12) + 1;
        if (month == 2){
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)){
                monthDays[1] = 29;
            }
        }
        int day = random.nextInt(monthDays[month - 1]) + 1;

        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        int second = random.nextInt(60);

        return LocalDateTime.of(year, month, day, hour, minute, second).format(formatter);
    }
}
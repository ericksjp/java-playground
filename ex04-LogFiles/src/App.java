/*
 * Um site de internet registra um log de acessos dos usuários. Um registro de log consiste no nome de usuário (apenas uma palavra)
 * e o instante em que o usuário acessou o site no padrão ISO 8601, separados por espaço, conforme exemplo.
 * Fazer um programa que leia o log de acessos a partir de um arquivo, e daí informe quantos usuários distintos acessaram o site.
 */

// Compilado com JETBRAINS INTELLIJ IDEA 2021.1.1 x64

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
public class App {
    public static void main(String[] args) throws Exception {

        Set<Log> logSet = new HashSet<Log>();

        // Gera um arquivo de log com a quantidade de registros especificada
        var fileLogs = GenerateLogs.generateLogFile(80000, "logs.txt");

        // Lê o arquivo de log e armazena os registros em um Set
        try (final var logs = new BufferedReader(new FileReader(fileLogs.getAbsoluteFile()))) {
            var line = logs.readLine();

            while (line != null) {
                var logInfo = line.split(" ");
                var userName = logInfo[0];
                var logHour = LocalDateTime.parse(logInfo[1], DateTimeFormatter.ISO_DATE_TIME);
                logSet.add(new Log(userName, logHour));
                line = logs.readLine();
            }

            // Exibe a quantidade de usuários distintos que acessaram o site
            System.out.println("Total users: " + logSet.size());

        } catch (Exception ex) {
            System.out.println("Erro! encerrando programa.");
        }
    }
}

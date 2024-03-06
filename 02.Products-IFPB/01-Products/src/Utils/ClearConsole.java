package Utils;

import java.io.IOException;

public class ClearConsole {
    public static void clear() {
        try {
            final String os = System.getProperty("os.name");

            ProcessBuilder processBuilder;
            if (os.contains("Windows")) {
                processBuilder = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                processBuilder = new ProcessBuilder("clear");
            }

            // Start the process
            Process process = processBuilder.inheritIO().start();

            // Wait for the process to finish (if needed)
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                // Handle any issues with the process execution
                System.out.println("Error clearing console");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error clearing console");
        }
    }
}

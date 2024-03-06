import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0){
            String pathFile = args[0];
            var commands = new ArrayList<String>();

            try (BufferedReader br = new BufferedReader(new FileReader(pathFile))){
                String line = br.readLine();
                while (line != null){
                    commands.add(line);
                    line = br.readLine();
                }
                var simpl = new Simpletron();
                simpl.insertCode(commands.stream().toArray(String[]::new));
                simpl.exec();
            } catch (Throwable e){
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            var simpl = new Simpletron();
            simpl.writeCode();
            simpl.exec();
        }
    }
}
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * Created by renatn on 06.05.2014.
 */
public class DirectoryScannerConsole {

    public static void main(String[] args) {

        System.out.println("Java Directory Scanner. Enter command: ");

        BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
        try {

            boolean done = false;
            while (!done) {
                System.out.print("> ");
                String commandLine = console.readLine().trim();
                if ("".equals(commandLine)) {
                    continue;
                }

                String[] options = commandLine.trim().toUpperCase().split(" ");
                System.out.println(Arrays.toString(options));

                CommandType commandType;
                try {
                    commandType = CommandType.valueOf(options[0]);
                } catch (IllegalArgumentException e) {
                    System.err.println();
                    System.err.println("Unknown command. Type HELP to see available commands.");
                    continue;
                }

                if (commandType == CommandType.EXIT) {
                    System.out.println("Session ended. Bye!");
                    done = true;
                } else if (commandType == CommandType.HELP) {
                    for (CommandType type : CommandType.values()) {
                        System.out.println(type.name());
                    }
                } else if (commandType == CommandType.SCAN) {
                    if (options.length < 2) {
                        System.err.println("You have to pass required arguments");
                        continue;
                    }
                    int i = 1;
                    while (i<options.length) {
                        String option = options[i++];
                        if (option.startsWith("-")) {
                            String value = options[i++];
                            System.out.println(option.substring(1)+" = "+value);
                        }
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void scan() {

    }
}

package com.renatn.sfour;

/**
 * Author: Renat Nasyrov
 * Date: 08.05.2014
 * Time: 11:24
 */
public class CommandFactory {

    public static Command create(CommandType type, Options options) {
        switch (type) {
            case SCAN:
                return new ScanCommand(options);
            case HELP:
                return new HelpCommand();
            case EXIT:
                return new Command() {
                    @Override
                    public void execute() {
                        System.out.println("Session ended. Bye!");
                    }
                };
        }
        throw new UnsupportedOperationException();
    }

}

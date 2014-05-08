package com.renatn.sfour;

/**
 * Author: Renat Nasyrov
 * Date: 08.05.2014
 * Time: 10:32
 */
public class HelpCommand implements Command {

    @Override
    public void execute() {
        for (CommandType type : CommandType.values()) {
            System.out.println(type.name());
        }
    }

}

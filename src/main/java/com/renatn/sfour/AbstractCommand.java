package com.renatn.sfour;

/**
 * Author: Renat Nasyrov <SBT-Nasyrov-RV@mail.ca.sbrf.ru>
 * Date: 08.05.2014
 * Time: 17:02
 */
public abstract class AbstractCommand implements Command {

    private final Options options;

    public AbstractCommand(Options options) {
        this.options = options;
    }

    public Options getOptions() {
        return options;
    }

}

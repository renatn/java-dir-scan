package com.renatn.sfour;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by renatn
 */
public class DirectoryScannerConsole {

    private static final Logger log = LoggerFactory.getLogger(DirectoryScannerConsole.class);

    private static final ExecutorService executorService = Executors.newCachedThreadPool();

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

                final Options options = Options.parse(commandLine);

                final CommandType commandType;
                try {
                    commandType = CommandType.valueOf(options.getCommand().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: Unknown command. Type HELP to see available commands.");
                    continue;
                }

                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        Command command = CommandFactory.create(commandType, options);
                        try {
                            command.execute();
                        } catch (RuntimeException e) {
                            System.out.println("Error: Scanner was not started due to following reasons: " + e.getMessage());
                            System.out.flush();
                        }
                    }
                });

                if (commandType == CommandType.EXIT) {
                    done = true;
                }

                TimeUnit.MILLISECONDS.sleep(200);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdownNow();
        }
    }


}

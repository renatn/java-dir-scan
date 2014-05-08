package com.renatn.sfour;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Author: Renat Nasyrov
 * Date: 08.05.2014
 * Time: 10:32
 */
public class ScanCommand extends AbstractCommand {

    private static final Logger log = LoggerFactory.getLogger(ScanCommand.class);

    private File inputDir; //сканируемая директория
    private File outputDir; // выходная директория
    private String mask; // маска для файлов, которые должны копироваться сканером
    private int waitInterval; // интервал, с которым сканер будет проверять заданную директорию
    private boolean includeSubfolders; //- включать или не включать обработку поддиректорий (если включать, то сканер должен сохранять структуру поддиректорий при копировании в выходную директорию)
    private boolean autoDelete; // удалять или не удалять файлы после копирования в сканируемой директории

    private HashMap<String, Long> history = new HashMap<String, Long>();

    public ScanCommand(Options options) {
        super(options);
    }

    @Override
    public void execute() {

        /* Валидируем переданные аргументы команды */
        validateAndSetOptions(getOptions());

        MDC.put("scanner", Thread.currentThread().getName());
        log.info("[{}] Start scanning {} (interval:{}, mask: {}, autoDelete:{}, includeSubfolders: {})",
                Thread.currentThread().getName(), inputDir, waitInterval, mask, autoDelete, includeSubfolders);
        System.out.println("Scanner [" + Thread.currentThread().getName() + "] is stared");

        while (!Thread.currentThread().isInterrupted()) {

            IOFileFilter subFoldersFilter = includeSubfolders ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE;
            Iterator<File> fileIterator = FileUtils.iterateFiles(inputDir, new WildcardFileFilter(mask), subFoldersFilter);

            while (fileIterator.hasNext()) {

                File fileSrc = fileIterator.next();

                // Проверяем изменился ли файл который мы уже копировали
                Long lastModified = history.get(fileSrc.getPath());
                if (lastModified != null && fileSrc.lastModified() == lastModified) {
                    continue;
                }

                String destinationPath = fileSrc.getPath().substring(inputDir.getPath().length());

                File fileDst = new File(outputDir.getPath() + destinationPath);
                try {
                    if (fileDst.exists()) {
                        log.warn("Destination file " + fileDst.getPath() + " already exists. Will be overwritten!");
                        FileUtils.deleteQuietly(fileDst);
                    }

                    if (autoDelete) {
                        FileUtils.moveFile(fileSrc, fileDst);
                    } else {
                        FileUtils.copyFile(fileSrc, fileDst);
                        history.put(fileSrc.getPath(), fileSrc.lastModified());
                    }
                    log.info("File " + fileSrc.getName() + " was copied to " + fileDst.getPath());

                } catch (IOException e) {
                    log.error("Cannot copy file +" + fileSrc.getPath(), e);
                }
            }

            try {
                TimeUnit.SECONDS.sleep(waitInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
        log.info("Scanning " + inputDir.getPath() + " is stopped");
        MDC.remove("scanner");
    }

    /**
     * Производит валидацию аргументов команды и в случае успеха проставляет соответстующие поля класса
     *
     * @param options Аргументы команды
     * @throws IllegalArgumentException
     */
    private void validateAndSetOptions(Options options) throws IllegalArgumentException {

        inputDir = new File(options.getStringOption("inputDir", true));
        if (!inputDir.exists()) {
            throw new IllegalArgumentException("Source directory " + inputDir.getPath() + " does not exists!!!");
        }

        outputDir = new File(options.getStringOption("outputDir", true));
        if (!outputDir.exists()) {
            throw new IllegalArgumentException("Destination directory " + outputDir.getPath() + " does not exists!!!");
        }

        mask = options.getStringOption("mask", true);

        waitInterval = options.getIntOption("waitInterval", true);
        if (waitInterval < 1) {
            throw new IllegalArgumentException("Incorrect value for waitInterval: " + waitInterval + " (must be positive int value)");
        }

        autoDelete = options.getBooleanOption("autoDelete", false);
        includeSubfolders = options.getBooleanOption("includeSubfolders", false);

    }

}



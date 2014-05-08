package com.renatn.sfour;

import org.hamcrest.core.StringEndsWith;
import org.junit.Test;

import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Author: Renat Nasyrov <SBT-Nasyrov-RV@mail.ca.sbrf.ru>
 * Date: 08.05.2014
 * Time: 17:37
 */
public class OptionsTest {

    @Test
    public void testParse() throws Exception {
        Options options = Options.parse("scan -inputDir C:\\Users\\renatn\\tmp\\00 -outputDir C:\\Users\\renatn\\tmp\\01 -waitInterval 3 -mask 1.tmp -autoDelete true -includeSubfolders true");
        assertThat(options.getCommand(), is("scan"));
        assertThat(options.getStringOption("inputDir", false), endsWith("00"));
        assertThat(options.getStringOption("outputDir", false), endsWith("01"));
        assertThat(options.getIntOption("waitInterval", false), is(3));
        assertThat(options.getStringOption("mask", false), is("1.tmp"));
        assertThat(options.getBooleanOption("autoDelete", false), is(true));
        assertThat(options.getBooleanOption("includeSubfolders", false), is(true));
    }

}

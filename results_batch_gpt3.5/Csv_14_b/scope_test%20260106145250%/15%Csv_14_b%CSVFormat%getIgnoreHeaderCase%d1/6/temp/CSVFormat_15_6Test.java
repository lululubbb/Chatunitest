package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVFormat.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

class CSVFormat_15_6Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_Default() {
        // The DEFAULT CSVFormat has ignoreHeaderCase = false
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        assertTrue(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_ExcelFormat() {
        // EXCEL modifies DEFAULT withIgnoreEmptyLines(false) and withAllowMissingColumnNames()
        // It does not change ignoreHeaderCase so should be false (from DEFAULT constructor)
        CSVFormat format = CSVFormat.EXCEL;
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_CustomViaReflection() throws Exception {
        // Use reflection to create a CSVFormat instance with ignoreHeaderCase = true and false
        // The constructor is private, so we use reflection to invoke it.

        Class<?> quoteModeClass = Class.forName("org.apache.commons.csv.CSVFormat$QuoteMode");

        Constructor<CSVFormat> constructor =
                CSVFormat.class.getDeclaredConstructor(
                        char.class, Character.class, quoteModeClass,
                        Character.class, Character.class,
                        boolean.class, boolean.class,
                        String.class, String.class,
                        Object[].class, String[].class,
                        boolean.class, boolean.class,
                        boolean.class, boolean.class,
                        boolean.class);

        constructor.setAccessible(true);

        // ignoreHeaderCase = true
        CSVFormat formatTrue = constructor.newInstance(
                ',', '"', null, null, null,
                false, true,
                "\r\n", null,
                null, null,
                false, false,
                true, false,
                false);

        assertTrue(formatTrue.getIgnoreHeaderCase());

        // ignoreHeaderCase = false
        CSVFormat formatFalse = constructor.newInstance(
                ',', '"', null, null, null,
                false, true,
                "\r\n", null,
                null, null,
                false, false,
                false, false,
                false);

        assertFalse(formatFalse.getIgnoreHeaderCase());
    }
}
package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class CSVFormat_11_3Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test getAllowMissingColumnNames returns correct value for default CSVFormat")
    void testGetAllowMissingColumnNames_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test getAllowMissingColumnNames returns true after withAllowMissingColumnNames(true)")
    void testGetAllowMissingColumnNames_TrueAfterWithAllowMissingColumnNames() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test getAllowMissingColumnNames returns false after withAllowMissingColumnNames(false)")
    void testGetAllowMissingColumnNames_FalseAfterWithAllowMissingColumnNames() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test getAllowMissingColumnNames on custom CSVFormat instance created by constructor")
    void testGetAllowMissingColumnNames_CustomInstance() throws Exception {
        // Using reflection to invoke private constructor with correct parameter types
        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat formatTrue = constructor.newInstance(
                ',', '"', null, null, null,
                false, false, "\r\n", null, null,
                false, true);
        assertTrue(formatTrue.getAllowMissingColumnNames());

        CSVFormat formatFalse = constructor.newInstance(
                ',', '"', null, null, null,
                false, false, "\r\n", null, null,
                false, false);
        assertFalse(formatFalse.getAllowMissingColumnNames());
    }
}
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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;

public class CSVFormat_5_2Test {

    @Test
    @Timeout(8000)
    public void testEquals_SameObject() {
        CSVFormat format = createCSVFormatInstance(',', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", "NULL", new String[]{"Header"}, true, true);

        assertTrue(format.equals(format));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NullObject() {
        CSVFormat format = createCSVFormatInstance(',', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", "NULL", new String[]{"Header"}, true, true);

        assertFalse(format.equals(null));
    }

    @Test
    @Timeout(8000)
    public void testEquals_DifferentClass() {
        CSVFormat format = createCSVFormatInstance(',', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", "NULL", new String[]{"Header"}, true, true);
        Object obj = new Object();

        assertFalse(format.equals(obj));
    }

    @Test
    @Timeout(8000)
    public void testEquals_EqualObjects() {
        CSVFormat format1 = createCSVFormatInstance(',', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", "NULL", new String[]{"Header"}, true, true);
        CSVFormat format2 = createCSVFormatInstance(',', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", "NULL", new String[]{"Header"}, true, true);

        assertTrue(format1.equals(format2));
    }

    @Test
    @Timeout(8000)
    public void testEquals_NotEqual_Delimiter() {
        CSVFormat format1 = createCSVFormatInstance(',', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", "NULL", new String[]{"Header"}, true, true);
        CSVFormat format2 = createCSVFormatInstance('|', '\"', QuoteMode.ALL, '#', '\\', true, false, "\r\n", "NULL", new String[]{"Header"}, true, true);

        assertFalse(format1.equals(format2));
    }

    private CSVFormat createCSVFormatInstance(char delimiter, char quoteChar, QuoteMode quoteMode, char commentStart,
                                              char escape, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines,
                                              String recordSeparator, String nullString, String[] header, boolean skipHeaderRecord,
                                              boolean allowMissingColumnNames) {
        try {
            Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class,
                    Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, String[].class,
                    boolean.class, boolean.class);
            constructor.setAccessible(true);
            return constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape, ignoreSurroundingSpaces,
                    ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord, allowMissingColumnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
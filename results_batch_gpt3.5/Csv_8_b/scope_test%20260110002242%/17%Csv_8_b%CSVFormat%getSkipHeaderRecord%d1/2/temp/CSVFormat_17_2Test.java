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

import java.lang.reflect.Constructor;

class CSVFormat_17_2Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_CustomTrue() throws Exception {
        CSVFormat format = createCSVFormatWithSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_CustomFalse() throws Exception {
        CSVFormat format = createCSVFormatWithSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord());
    }

    // Helper method to create CSVFormat instance with skipHeaderRecord set using reflection
    private CSVFormat createCSVFormatWithSkipHeaderRecord(boolean skipHeaderRecord) throws Exception {
        Class<?> quoteClass;
        try {
            quoteClass = Class.forName("org.apache.commons.csv.CSVFormat$Quote");
        } catch (ClassNotFoundException e) {
            quoteClass = null;
        }

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteClass, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        return constructor.newInstance(
                defaultFormat.getDelimiter(),
                defaultFormat.getQuoteChar(),
                defaultFormat.getQuotePolicy(),
                defaultFormat.getCommentStart(),
                defaultFormat.getEscape(),
                defaultFormat.getIgnoreSurroundingSpaces(),
                defaultFormat.getIgnoreEmptyLines(),
                defaultFormat.getRecordSeparator(),
                defaultFormat.getNullString(),
                defaultFormat.getHeader(),
                skipHeaderRecord);
    }
}
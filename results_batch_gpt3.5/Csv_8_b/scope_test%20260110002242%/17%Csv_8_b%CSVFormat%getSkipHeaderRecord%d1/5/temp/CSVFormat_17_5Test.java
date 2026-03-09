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

class CSVFormat_17_5Test {

    private CSVFormat createCSVFormat(boolean skipHeaderRecord) throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String[].class, String.class, boolean.class);
        constructor.setAccessible(true);
        return constructor.newInstance(',', '"', null, null, null,
                false, true, "\r\n", null, null, skipHeaderRecord);
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordTrue() throws Exception {
        CSVFormat format = createCSVFormat(true);
        assertTrue(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordFalse() throws Exception {
        CSVFormat format = createCSVFormat(false);
        assertFalse(format.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordDefaultFalse() {
        assertFalse(CSVFormat.DEFAULT.getSkipHeaderRecord());
    }

}
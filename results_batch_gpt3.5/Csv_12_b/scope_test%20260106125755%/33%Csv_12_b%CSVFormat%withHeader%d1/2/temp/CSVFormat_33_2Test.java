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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_33_2Test {

    @Test
    @Timeout(8000)
    void testWithHeader_NullHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader((String[]) null);
        assertNotNull(result);
        assertNull(result.getHeader());
        // original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_EmptyHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withHeader(new String[0]);
        assertNotNull(result);
        assertNotNull(result.getHeader());
        assertEquals(0, result.getHeader().length);
        // original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_SingleHeader() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[]{"col1"};
        CSVFormat result = format.withHeader(header);
        assertNotNull(result);
        assertArrayEquals(header, result.getHeader());
        // original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_MultipleHeaders() {
        CSVFormat format = CSVFormat.DEFAULT;
        String[] header = new String[]{"col1", "col2", "col3"};
        CSVFormat result = format.withHeader(header);
        assertNotNull(result);
        assertArrayEquals(header, result.getHeader());
        // original format unchanged
        assertNull(format.getHeader());
    }

    @Test
    @Timeout(8000)
    void testWithHeader_OriginalFormatImmutable() {
        CSVFormat format = CSVFormat.DEFAULT.withHeader("a", "b");
        CSVFormat newFormat = format.withHeader("x", "y");
        assertArrayEquals(new String[]{"a","b"}, format.getHeader());
        assertArrayEquals(new String[]{"x","y"}, newFormat.getHeader());
        assertNotSame(format, newFormat);
    }
}
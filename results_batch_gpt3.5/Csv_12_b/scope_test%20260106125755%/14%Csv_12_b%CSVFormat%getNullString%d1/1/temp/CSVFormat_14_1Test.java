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

class CSVFormat_14_1Test {

    @Test
    @Timeout(8000)
    void testGetNullStringWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullStringWhenSet() {
        String nullStr = "NULL";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr);
        assertEquals(nullStr, format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullStringOnNewFormatWithNullString() {
        CSVFormat format = CSVFormat.newFormat(';').withNullString("N/A");
        assertEquals("N/A", format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullStringOnDefaultFormat() {
        assertNull(CSVFormat.DEFAULT.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullStringOnModifiedFormat() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("nullValue").withDelimiter('|');
        assertEquals("nullValue", format.getNullString());
    }
}
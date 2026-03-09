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

class CSVFormat_13_1Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullString() {
        String nullStr = "NULL";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr);
        assertEquals(nullStr, format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNull() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        assertNull(format.getNullString());
    }
}
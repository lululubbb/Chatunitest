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

class CSVFormat_22_1Test {

    @Test
    @Timeout(8000)
    void testIsQuoting_withNonNullQuoteChar() {
        // Using DEFAULT which has DOUBLE_QUOTE_CHAR as quoteChar
        CSVFormat format = CSVFormat.DEFAULT;
        assertTrue(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuoting_withNullQuoteChar() {
        // Using MYSQL which has quoteChar set to null
        CSVFormat format = CSVFormat.MYSQL;
        assertFalse(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuoting_withQuoteCharSetToNullViaWithQuoteChar() {
        // Create a format from DEFAULT and set quoteChar to null explicitly
        CSVFormat format = CSVFormat.DEFAULT.withQuoteChar((Character) null);
        assertFalse(format.isQuoting());
    }

    @Test
    @Timeout(8000)
    void testIsQuoting_withQuoteCharSetToNonNullViaWithQuoteChar() {
        // Create a format from MYSQL and set quoteChar to a non-null value
        CSVFormat format = CSVFormat.MYSQL.withQuoteChar('\'');
        assertTrue(format.isQuoting());
    }
}
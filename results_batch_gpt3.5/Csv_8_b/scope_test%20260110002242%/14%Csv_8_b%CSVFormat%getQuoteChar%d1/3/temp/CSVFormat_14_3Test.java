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

class CSVFormat_14_3Test {

    @Test
    @Timeout(8000)
    void testGetQuoteChar_whenQuoteCharIsNonNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        Character quoteChar = format.getQuoteChar();
        assertNotNull(quoteChar);
        assertEquals(Character.valueOf('"'), quoteChar);
    }

    @Test
    @Timeout(8000)
    void testGetQuoteChar_whenQuoteCharIsNull() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteChar((Character) null);
        Character quoteChar = format.getQuoteChar();
        assertNull(quoteChar);
    }
}
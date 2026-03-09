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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

class CSVFormat_20_4Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set the private quoteMode field to null explicitly
        Field quoteModeField = CSVFormat.class.getDeclaredField("quoteMode");
        quoteModeField.setAccessible(true);

        // CSVFormat is immutable, but the field is final and private.
        // Since the field is final, to modify it via reflection, we can use Unsafe or
        // create a new instance via constructor, but here we forcibly modify the field.

        // Remove final modifier if possible (Java 12+ restricts this, so skip modifiers change)
        // Just set accessible and set the field value anyway (works in many JVMs)

        quoteModeField.set(format, null);

        assertNull(format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_WithQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
        assertEquals(QuoteMode.ALL, format.getQuoteMode());
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_NullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        assertNull(format.getQuoteMode());
    }
}
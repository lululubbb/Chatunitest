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

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class CSVFormat_20_4Test {

    @Test
    @Timeout(8000)
    void testGetQuoteMode_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get the "quoteMode" field from the CSVFormat instance's class hierarchy
        Field quoteModeField = null;
        Class<?> clazz = format.getClass();
        while (clazz != null) {
            try {
                quoteModeField = clazz.getDeclaredField("quoteMode");
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        assertNotNull(quoteModeField, "Field 'quoteMode' not found");

        quoteModeField.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quoteModeField, quoteModeField.getModifiers() & ~Modifier.FINAL);

        // Set the quoteMode field to null on the CSVFormat instance
        quoteModeField.set(format, null);

        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "Default CSVFormat quoteMode should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_CustomQuoteMode() {
        QuoteMode customMode = QuoteMode.ALL;
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(customMode);
        QuoteMode mode = format.getQuoteMode();
        assertEquals(customMode, mode, "CSVFormat quoteMode should be the custom set QuoteMode");
    }

    @Test
    @Timeout(8000)
    void testGetQuoteMode_NullQuoteMode() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(null);
        QuoteMode mode = format.getQuoteMode();
        assertNull(mode, "CSVFormat quoteMode should be null when explicitly set to null");
    }
}
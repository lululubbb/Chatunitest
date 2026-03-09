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
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_15_5Test {

    @Test
    @Timeout(8000)
    public void testGetQuotePolicy_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to set the private final field quotePolicy to null
        Field quotePolicyField = CSVFormat.class.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);

        // Remove final modifier using reflection (Java 8+)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(quotePolicyField, quotePolicyField.getModifiers() & ~Modifier.FINAL);

        quotePolicyField.set(format, null);

        Quote quotePolicy = format.getQuotePolicy();
        assertNull(quotePolicy, "Default CSVFormat quotePolicy should be null");
    }

    @Test
    @Timeout(8000)
    public void testGetQuotePolicy_WithQuotePolicy() {
        Quote customQuotePolicy = Quote.MINIMAL; // use an existing Quote enum constant
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(customQuotePolicy);
        Quote quotePolicy = format.getQuotePolicy();
        assertSame(customQuotePolicy, quotePolicy, "QuotePolicy should be the one set via withQuotePolicy");
    }

    @Test
    @Timeout(8000)
    public void testGetQuotePolicy_NullQuotePolicy() {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(null);
        Quote quotePolicy = format.getQuotePolicy();
        assertNull(quotePolicy, "QuotePolicy can be set to null");
    }
}
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class CSVFormat_15_3Test {

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Create a new CSVFormat instance by copying fields from DEFAULT but with quotePolicy null
        CSVFormat newFormat = createCSVFormatWithNullQuotePolicy(format);

        Quote quotePolicy = newFormat.getQuotePolicy();
        assertNull(quotePolicy, "Default CSVFormat should have null quotePolicy");
    }

    private CSVFormat createCSVFormatWithNullQuotePolicy(CSVFormat original) throws Exception {
        Constructor<CSVFormat> ctor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        ctor.setAccessible(true);

        return ctor.newInstance(
                getFieldValue(original, "delimiter"),
                getFieldValue(original, "quoteChar"),
                null, // quotePolicy set to null
                getFieldValue(original, "commentStart"),
                getFieldValue(original, "escape"),
                getFieldValue(original, "ignoreSurroundingSpaces"),
                getFieldValue(original, "ignoreEmptyLines"),
                getFieldValue(original, "recordSeparator"),
                getFieldValue(original, "nullString"),
                getFieldValue(original, "header"),
                getFieldValue(original, "skipHeaderRecord")
        );
    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(CSVFormat instance, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_WithQuotePolicy() {
        Quote customQuotePolicy = Quote.ALL;
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(customQuotePolicy);
        Quote quotePolicy = format.getQuotePolicy();
        assertEquals(customQuotePolicy, quotePolicy, "Quote policy should be the one set via withQuotePolicy");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_NullQuotePolicy() {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(null);
        Quote quotePolicy = format.getQuotePolicy();
        assertNull(quotePolicy, "Quote policy should be null when explicitly set to null");
    }
}
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

class CSVFormat_15_6Test {

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_DefaultFormat() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Defensive: cannot set final fields via reflection reliably, so just assert the current value
        Quote quotePolicy = format.getQuotePolicy();
        assertNull(quotePolicy, "Default format quotePolicy should be null");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_CustomQuotePolicy() throws Exception {
        Quote customQuotePolicy = Quote.MINIMAL;

        CSVFormat customFormat = createCSVFormatWithQuotePolicy(CSVFormat.DEFAULT, customQuotePolicy);

        Quote quotePolicy = customFormat.getQuotePolicy();
        assertSame(customQuotePolicy, quotePolicy, "Quote policy should be the custom one set");
    }

    @Test
    @Timeout(8000)
    void testGetQuotePolicy_NullQuotePolicy() throws Exception {
        CSVFormat nullQuotePolicyFormat = createCSVFormatWithQuotePolicy(CSVFormat.DEFAULT, null);

        Quote quotePolicy = nullQuotePolicyFormat.getQuotePolicy();
        assertNull(quotePolicy, "Quote policy explicitly set to null should be null");
    }

    // Helper method to create a new CSVFormat instance copying all fields except quotePolicy
    private CSVFormat createCSVFormatWithQuotePolicy(CSVFormat baseFormat, Quote quotePolicy) throws Exception {
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        Field headerField = CSVFormat.class.getDeclaredField("header");
        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");

        delimiterField.setAccessible(true);
        quoteCharField.setAccessible(true);
        commentStartField.setAccessible(true);
        escapeField.setAccessible(true);
        ignoreSurroundingSpacesField.setAccessible(true);
        ignoreEmptyLinesField.setAccessible(true);
        recordSeparatorField.setAccessible(true);
        nullStringField.setAccessible(true);
        headerField.setAccessible(true);
        skipHeaderRecordField.setAccessible(true);

        char delimiter = delimiterField.getChar(baseFormat);
        Character quoteChar = (Character) quoteCharField.get(baseFormat);
        Character commentStart = (Character) commentStartField.get(baseFormat);
        Character escape = (Character) escapeField.get(baseFormat);
        boolean ignoreSurroundingSpaces = ignoreSurroundingSpacesField.getBoolean(baseFormat);
        boolean ignoreEmptyLines = ignoreEmptyLinesField.getBoolean(baseFormat);
        String recordSeparator = (String) recordSeparatorField.get(baseFormat);
        String nullString = (String) nullStringField.get(baseFormat);
        String[] header = (String[]) headerField.get(baseFormat);
        boolean skipHeaderRecord = skipHeaderRecordField.getBoolean(baseFormat);

        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quotePolicy,
                commentStart,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                skipHeaderRecord);
    }
}
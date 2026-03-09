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

import java.lang.reflect.Field;

class CSVFormat_34_4Test {

    @Test
    @Timeout(8000)
    void testWithNullString() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        String newNullString = "NULL";

        CSVFormat modified = original.withNullString(newNullString);

        assertNotNull(modified);
        assertNotSame(original, modified);

        // Using reflection to access private field nullString
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        String originalNullString = (String) nullStringField.get(original);
        String modifiedNullString = (String) nullStringField.get(modified);

        assertNull(originalNullString);
        assertEquals(newNullString, modifiedNullString);

        // Verify other fields remain unchanged
        Field delimiterField = CSVFormat.class.getDeclaredField("delimiter");
        delimiterField.setAccessible(true);
        assertEquals(delimiterField.getChar(original), delimiterField.getChar(modified));

        Field quoteCharField = CSVFormat.class.getDeclaredField("quoteChar");
        quoteCharField.setAccessible(true);
        assertEquals((Character) quoteCharField.get(original), (Character) quoteCharField.get(modified));

        Field quotePolicyField = CSVFormat.class.getDeclaredField("quotePolicy");
        quotePolicyField.setAccessible(true);
        assertEquals(quotePolicyField.get(original), quotePolicyField.get(modified));

        Field commentStartField = CSVFormat.class.getDeclaredField("commentStart");
        commentStartField.setAccessible(true);
        assertEquals((Character) commentStartField.get(original), (Character) commentStartField.get(modified));

        Field escapeField = CSVFormat.class.getDeclaredField("escape");
        escapeField.setAccessible(true);
        assertEquals((Character) escapeField.get(original), (Character) escapeField.get(modified));

        Field ignoreSurroundingSpacesField = CSVFormat.class.getDeclaredField("ignoreSurroundingSpaces");
        ignoreSurroundingSpacesField.setAccessible(true);
        assertEquals(ignoreSurroundingSpacesField.getBoolean(original), ignoreSurroundingSpacesField.getBoolean(modified));

        Field ignoreEmptyLinesField = CSVFormat.class.getDeclaredField("ignoreEmptyLines");
        ignoreEmptyLinesField.setAccessible(true);
        assertEquals(ignoreEmptyLinesField.getBoolean(original), ignoreEmptyLinesField.getBoolean(modified));

        Field recordSeparatorField = CSVFormat.class.getDeclaredField("recordSeparator");
        recordSeparatorField.setAccessible(true);
        assertEquals(recordSeparatorField.get(original), recordSeparatorField.get(modified));

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        assertArrayEquals((String[]) headerField.get(original), (String[]) headerField.get(modified));

        Field skipHeaderRecordField = CSVFormat.class.getDeclaredField("skipHeaderRecord");
        skipHeaderRecordField.setAccessible(true);
        assertEquals(skipHeaderRecordField.getBoolean(original), skipHeaderRecordField.getBoolean(modified));
    }
}
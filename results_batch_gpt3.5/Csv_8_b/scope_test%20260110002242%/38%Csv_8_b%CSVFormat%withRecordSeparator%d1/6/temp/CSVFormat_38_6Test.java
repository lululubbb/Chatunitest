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

class CSVFormatWithRecordSeparatorTest {

    private static String getCRLF() {
        try {
            Field crlfField = Constants.class.getDeclaredField("CRLF");
            crlfField.setAccessible(true);
            return (String) crlfField.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char sep = '\n';

        CSVFormat updated = original.withRecordSeparator(sep);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(sep), updated.getRecordSeparator());
        // Original should remain unchanged
        assertEquals(getCRLF(), original.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_edgeCases() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with typical line break characters
        char[] separators = { '\r', '\n', '\t', ',', '|' };

        for (char sep : separators) {
            CSVFormat updated = original.withRecordSeparator(sep);
            assertNotNull(updated);
            assertEquals(String.valueOf(sep), updated.getRecordSeparator());
            assertNotSame(original, updated);
        }

        // Test with non-printable character
        char nonPrintable = 0x1F;
        CSVFormat updated = original.withRecordSeparator(nonPrintable);
        assertEquals(String.valueOf(nonPrintable), updated.getRecordSeparator());
    }
}
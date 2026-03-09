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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;

class CSVFormatWithRecordSeparatorTest {

    private CSVFormat baseFormat;

    @BeforeEach
    void setUp() {
        baseFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_validSeparators() {
        // Test with common record separators
        char[] separators = {'\n', '\r', '\u2028', '\u2029', '\u0085', '|'};
        for (char sep : separators) {
            CSVFormat newFormat = baseFormat.withRecordSeparator(sep);
            assertNotNull(newFormat);
            assertEquals(String.valueOf(sep), newFormat.getRecordSeparator());
            // Original format remains unchanged
            assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());
        }
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_emptyChar() {
        // Use a whitespace char as separator
        char sep = ' ';
        CSVFormat newFormat = baseFormat.withRecordSeparator(sep);
        assertNotNull(newFormat);
        assertEquals(" ", newFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_nonPrintable() {
        // Use a non-printable char
        char sep = 0x1F;
        CSVFormat newFormat = baseFormat.withRecordSeparator(sep);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(sep), newFormat.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_invokesWithRecordSeparatorString() throws Exception {
        // Use reflection to invoke public withRecordSeparator(String)
        Method method = CSVFormat.class.getMethod("withRecordSeparator", String.class);

        String testSeparator = "\n";
        CSVFormat result = (CSVFormat) method.invoke(baseFormat, testSeparator);
        assertNotNull(result);
        assertEquals(testSeparator, result.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_andStringConsistency() throws Exception {
        char sepChar = '\n';
        CSVFormat fromChar = baseFormat.withRecordSeparator(sepChar);

        Method method = CSVFormat.class.getMethod("withRecordSeparator", String.class);
        CSVFormat fromString = (CSVFormat) method.invoke(baseFormat, String.valueOf(sepChar));

        assertEquals(fromChar, fromString);
        assertEquals(fromChar.getRecordSeparator(), fromString.getRecordSeparator());
    }

}
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

class CSVFormat_38_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newSeparator = '\n';

        CSVFormat updated = original.withRecordSeparator(newSeparator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(newSeparator), updated.getRecordSeparator());

        // Original should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_different() {
        CSVFormat original = CSVFormat.DEFAULT;
        char newSeparator = '\r';

        CSVFormat updated = original.withRecordSeparator(newSeparator);

        assertEquals(String.valueOf(newSeparator), updated.getRecordSeparator());
        assertNotEquals(original.getRecordSeparator(), updated.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_sameAsOriginal() {
        CSVFormat original = CSVFormat.DEFAULT;
        String origSeparator = original.getRecordSeparator();
        assertNotNull(origSeparator);
        char origChar = origSeparator.charAt(0);

        // Since withRecordSeparator(char) returns the same instance if the separator is the same,
        // we assert that the returned instance is the same and the separator string is unchanged.
        CSVFormat updated = original.withRecordSeparator(origChar);

        assertEquals(origSeparator, updated.getRecordSeparator());
        assertSame(original, updated);
    }
}
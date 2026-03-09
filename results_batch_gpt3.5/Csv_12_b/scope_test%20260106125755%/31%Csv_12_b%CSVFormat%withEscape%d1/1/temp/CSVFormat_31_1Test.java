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

public class CSVFormat_31_1Test {

    @Test
    @Timeout(8000)
    public void testWithEscapePrimitiveChar() {
        // Arrange
        CSVFormat original = CSVFormat.DEFAULT;

        // Act
        CSVFormat result = original.withEscape('\\');

        // Assert
        assertNotNull(result);
        assertEquals(Character.valueOf('\\'), result.getEscapeCharacter());
        // Original remains unchanged (immutability)
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapePrimitiveCharDifferent() {
        CSVFormat original = CSVFormat.DEFAULT.withEscape(Character.valueOf('!'));
        CSVFormat result = original.withEscape(Character.valueOf('?'));
        assertNotNull(result);
        assertEquals(Character.valueOf('?'), result.getEscapeCharacter());
        assertEquals(Character.valueOf('!'), original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapePrimitiveCharSameValue() {
        CSVFormat original = CSVFormat.DEFAULT.withEscape(Character.valueOf('^'));
        CSVFormat result = original.withEscape(Character.valueOf('^'));
        assertSame(original, result, "withEscape should return same instance if escape char is same");
    }

}
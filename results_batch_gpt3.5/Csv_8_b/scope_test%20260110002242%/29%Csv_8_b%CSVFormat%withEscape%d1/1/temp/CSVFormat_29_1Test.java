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
import java.lang.reflect.Method;

public class CSVFormat_29_1Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeChar() throws Exception {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        // Act
        CSVFormat result = format.withEscape(escapeChar);

        // Assert
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscape());
        // Original format should be unchanged (immutability)
        assertNull(format.getEscape());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeUsingReflection() throws Exception {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT;
        Character escapeChar = '"';

        // Use reflection to get the public withEscape(Character) method
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        // Act
        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(format, escapeChar);

        // Assert
        assertNotNull(result);
        assertEquals(escapeChar, result.getEscape());
        assertNull(format.getEscape());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeDifferentChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = 'e';

        CSVFormat result = format.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscape());
        assertNull(format.getEscape());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeSameCharReturnsNewInstance() {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('x');
        char escapeChar = 'x';

        CSVFormat result = format.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscape());
        assertNotSame(format, result);
    }
}
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

class CSVFormat_31_5Test {

    @Test
    @Timeout(8000)
    void testWithEscape_PrimitiveChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat resultFormat = baseFormat.withEscape(escapeChar);

        assertNotNull(resultFormat);
        assertEquals(Character.valueOf(escapeChar), resultFormat.getEscapeCharacter());
        assertNotSame(baseFormat, resultFormat);
    }

    @Test
    @Timeout(8000)
    void testWithEscape_NullCharacter() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Use reflection to invoke public withEscape(Character) method with null
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);
        CSVFormat resultFormat = (CSVFormat) withEscapeCharMethod.invoke(baseFormat, (Object) null);

        assertNotNull(resultFormat);
        assertNull(resultFormat.getEscapeCharacter());
        assertNotSame(baseFormat, resultFormat);
    }

    @Test
    @Timeout(8000)
    void testWithEscape_CharacterObject() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character escapeChar = Character.valueOf('\\');

        // Use reflection to invoke public withEscape(Character) method with valid character
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);
        CSVFormat resultFormat = (CSVFormat) withEscapeCharMethod.invoke(baseFormat, escapeChar);

        assertNotNull(resultFormat);
        assertEquals(escapeChar, resultFormat.getEscapeCharacter());
        assertNotSame(baseFormat, resultFormat);
    }
}
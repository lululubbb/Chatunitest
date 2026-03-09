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
import java.lang.reflect.Method;

public class CSVFormat_31_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = baseFormat.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // The original should remain unchanged (immutability check)
        assertNull(baseFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeCharDifferent() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = 'X';

        CSVFormat result = baseFormat.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        assertNull(baseFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeCharReflection() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = 'Z';

        Method withEscapeMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeMethod.setAccessible(true);
        CSVFormat result = (CSVFormat) withEscapeMethod.invoke(baseFormat, Character.valueOf(escapeChar));

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        assertNull(baseFormat.getEscapeCharacter());
    }
}
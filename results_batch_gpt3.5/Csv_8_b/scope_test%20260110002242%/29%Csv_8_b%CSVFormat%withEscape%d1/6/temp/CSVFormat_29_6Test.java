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
import static org.mockito.Mockito.*;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_29_6Test {

    private CSVFormat csvFormatDefault;

    @BeforeEach
    public void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_char_nonNull() {
        char escapeChar = '\\';
        CSVFormat result = csvFormatDefault.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscape());
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_char_nullEscape() {
        try {
            Method withEscapeMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
            withEscapeMethod.setAccessible(true);

            CSVFormat result = (CSVFormat) withEscapeMethod.invoke(csvFormatDefault, new Object[] { null });
            assertNotNull(result);
            assertNull(result.getEscape());
        } catch (Exception e) {
            fail("Reflection invocation failed: " + e.getMessage());
        }
    }
}
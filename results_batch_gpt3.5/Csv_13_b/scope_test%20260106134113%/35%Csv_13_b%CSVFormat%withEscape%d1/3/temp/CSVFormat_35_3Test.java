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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_35_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = format.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // The original format should remain unchanged (immutability)
        assertNull(format.getEscapeCharacter());
        assertNotSame(format, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeCharacterNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke public withEscape(Character) with null
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(format, new Object[]{null});

        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        // Should return the same instance if the escape character is not changed (null to null)
        assertSame(format, result);
    }
}
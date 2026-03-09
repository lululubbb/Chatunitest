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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_35_4Test {

    @Test
    @Timeout(8000)
    void testWithEscapeChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Call withEscape(char) which calls withEscape(Character)
        CSVFormat resultFormat = baseFormat.withEscape('\\');

        assertNotNull(resultFormat);
        assertNotSame(baseFormat, resultFormat);
        assertEquals(Character.valueOf('\\'), resultFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharacterNull() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        // Passing null Character should return a format with null escapeCharacter
        CSVFormat resultFormat = (CSVFormat) withEscapeCharMethod.invoke(baseFormat, (Object) null);

        assertNotNull(resultFormat);
        assertNotSame(baseFormat, resultFormat);
        assertNull(resultFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharacterNonNull() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        Character escapeChar = 'e';
        CSVFormat resultFormat = (CSVFormat) withEscapeCharMethod.invoke(baseFormat, escapeChar);

        assertNotNull(resultFormat);
        assertNotSame(baseFormat, resultFormat);
        assertEquals(escapeChar, resultFormat.getEscapeCharacter());
    }
}
package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_50_3Test {

    @Test
    @Timeout(8000)
    void testWithEscapePrimitiveChar() {
        CSVFormat format = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = format.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Original format is immutable, so result should not be the same instance
        assertNotSame(format, result);
    }

    @Test
    @Timeout(8000)
    void testWithEscapeCharacterObject() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Access public method withEscape(Character) directly (no need for reflection)
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        // Test with null Character
        CSVFormat resultNull = (CSVFormat) withEscapeCharMethod.invoke(format, new Object[]{null});
        assertNotNull(resultNull);
        assertNull(resultNull.getEscapeCharacter());

        // Test with non-null Character
        Character escapeChar = Character.valueOf('\\');
        CSVFormat resultChar = (CSVFormat) withEscapeCharMethod.invoke(format, escapeChar);
        assertNotNull(resultChar);
        assertEquals(escapeChar, resultChar.getEscapeCharacter());
        assertNotSame(format, resultChar);

        // Test with same escape character as original, should return same instance
        Character originalEscapeChar = format.getEscapeCharacter();
        if (originalEscapeChar == null) {
            // If original is null, test with null to get same instance
            CSVFormat sameEscapeFormat = (CSVFormat) withEscapeCharMethod.invoke(format, new Object[]{null});
            assertSame(format, sameEscapeFormat);
        } else {
            CSVFormat sameEscapeFormat = (CSVFormat) withEscapeCharMethod.invoke(format, originalEscapeChar);
            assertSame(format, sameEscapeFormat);
        }
    }
}
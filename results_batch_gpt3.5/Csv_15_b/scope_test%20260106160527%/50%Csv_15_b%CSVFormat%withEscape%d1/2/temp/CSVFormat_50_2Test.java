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

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_primitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Ensure original is not modified (immutability)
        assertNotEquals(original.getEscapeCharacter(), result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_Character_null() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Using reflection to invoke public withEscape(Character)
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(original, new Object[]{(Character) null});

        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        assertNotSame(original, result);
    }

    @Test
    @Timeout(8000)
    void testWithEscape_Character_nonNull() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        Character escapeChar = Character.valueOf('"');

        // Using reflection to invoke public withEscape(Character)
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        CSVFormat result = (CSVFormat) withEscapeCharMethod.invoke(original, escapeChar);

        assertNotNull(result);
        assertEquals(escapeChar, result.getEscapeCharacter());
        assertNotSame(original, result);
    }
}
package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_48_5Test {

    @Test
    @Timeout(8000)
    void testWithEscape_PrimitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat updated = original.withEscape(escapeChar);

        assertNotNull(updated);
        assertEquals(Character.valueOf(escapeChar), updated.getEscapeCharacter());
        // Original should remain unchanged (immutability)
        assertNull(original.getEscapeCharacter());
        // The updated instance should not be the same instance as original
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithEscape_NullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat updated = original.withEscape((Character) null);

        assertNotNull(updated);
        assertNull(updated.getEscapeCharacter());
        // Original should remain unchanged (immutability)
        assertNull(original.getEscapeCharacter());
        assertNotSame(original, updated);
    }

    @Test
    @Timeout(8000)
    void testWithEscape_CharacterObject() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Using reflection to invoke public withEscape(Character) method
        java.lang.reflect.Method method = CSVFormat.class.getMethod("withEscape", Character.class);

        Character escapeChar = Character.valueOf('\\');
        CSVFormat updated = (CSVFormat) method.invoke(original, escapeChar);

        assertNotNull(updated);
        assertEquals(escapeChar, updated.getEscapeCharacter());
        assertNull(original.getEscapeCharacter());
        assertNotSame(original, updated);
    }

}
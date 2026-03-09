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

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_PrimitiveChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Ensure original is unchanged (immutability)
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_CharacterObject() {
        CSVFormat original = CSVFormat.DEFAULT;
        Character escapeChar = Character.valueOf('"');

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(escapeChar, result.getEscapeCharacter());
        // Original unchanged
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_NullCharacter() {
        CSVFormat original = CSVFormat.DEFAULT;

        CSVFormat result = original.withEscape((Character) null);

        assertNotNull(result);
        assertNull(result.getEscapeCharacter());
        // Original unchanged
        assertNull(original.getEscapeCharacter());
    }
}
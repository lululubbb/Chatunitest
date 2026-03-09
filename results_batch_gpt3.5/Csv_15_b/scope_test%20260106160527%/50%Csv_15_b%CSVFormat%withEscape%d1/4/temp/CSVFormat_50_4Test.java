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

class CSVFormat_50_4Test {

    @Test
    @Timeout(8000)
    void testWithEscape_withEscapeChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // original should remain unchanged
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withDifferentEscapeChar() {
        CSVFormat original = CSVFormat.DEFAULT;
        char escapeChar = 'X';

        CSVFormat result = original.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        assertNull(original.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withSameEscapeCharReturnsSameInstance() throws Exception {
        // Create a CSVFormat instance with escape 'E' via withEscape(Character)
        CSVFormat original = CSVFormat.DEFAULT.withEscape(Character.valueOf('E'));

        CSVFormat result = original.withEscape('E');

        assertSame(original, result);
    }
}
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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_50_6Test {

    @Test
    @Timeout(8000)
    void testWithEscape_withEscapeChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = baseFormat.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Original instance should remain unchanged (immutability)
        assertNull(baseFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withDifferentEscapeChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '"';

        CSVFormat result = baseFormat.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withSameEscapeCharReturnsSameInstance() {
        // Create a CSVFormat instance with escape character set
        CSVFormat formatWithEscape = CSVFormat.DEFAULT.withEscape('\\');

        // Call withEscape again with same character
        CSVFormat result = formatWithEscape.withEscape('\\');

        // Should return the same instance (optimization)
        assertSame(formatWithEscape, result);
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withNullEscapeCharacter() {
        // Using the withEscape(Character) method to set null explicitly
        CSVFormat format = CSVFormat.DEFAULT.withEscape((Character) null);
        assertNull(format.getEscapeCharacter());

        // Now call withEscape(char) with a character
        CSVFormat result = format.withEscape('e');
        assertEquals(Character.valueOf('e'), result.getEscapeCharacter());
    }
}
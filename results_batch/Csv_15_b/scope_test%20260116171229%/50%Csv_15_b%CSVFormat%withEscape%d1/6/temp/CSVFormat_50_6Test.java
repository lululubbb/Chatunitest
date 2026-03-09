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

public class CSVFormat_50_6Test {

    @Test
    @Timeout(8000)
    void testWithEscape_withVariousChars() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with normal escape char
        CSVFormat result = baseFormat.withEscape('\\');
        assertNotNull(result);
        assertEquals(Character.valueOf('\\'), result.getEscapeCharacter());

        // Test with quote char as escape
        result = baseFormat.withEscape('"');
        assertNotNull(result);
        assertEquals(Character.valueOf('"'), result.getEscapeCharacter());

        // Test with comma as escape (unusual but valid)
        result = baseFormat.withEscape(',');
        assertNotNull(result);
        assertEquals(Character.valueOf(','), result.getEscapeCharacter());

        // Test with non-printable char as escape
        result = baseFormat.withEscape('\u0000');
        assertNotNull(result);
        assertEquals(Character.valueOf('\u0000'), result.getEscapeCharacter());

        // Test with same escape char as current escapeCharacter in baseFormat (which may be null)
        Character currentEscape = baseFormat.getEscapeCharacter();
        if (currentEscape != null) {
            result = baseFormat.withEscape(currentEscape);
            assertNotNull(result);
            assertEquals(currentEscape, result.getEscapeCharacter());
        }
    }

    @Test
    @Timeout(8000)
    void testWithEscape_doesNotModifyOriginal() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character originalEscape = baseFormat.getEscapeCharacter();

        CSVFormat modified = baseFormat.withEscape('x');
        assertNotNull(modified);
        assertEquals(Character.valueOf('x'), modified.getEscapeCharacter());

        // Original should remain unchanged
        assertEquals(originalEscape, baseFormat.getEscapeCharacter());
    }
}
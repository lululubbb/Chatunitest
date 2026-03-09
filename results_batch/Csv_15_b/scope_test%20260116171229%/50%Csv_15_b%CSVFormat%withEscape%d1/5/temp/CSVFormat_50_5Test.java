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

public class CSVFormat_50_5Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_char() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with a normal escape character
        char escapeChar = '\\';
        CSVFormat result = baseFormat.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());

        // Test with a different escape character
        escapeChar = '\"';
        result = baseFormat.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());

        // Test with a control character as escape
        escapeChar = '\n';
        result = baseFormat.withEscape(escapeChar);
        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());

        // Test that original instance is not modified (immutability)
        assertNull(baseFormat.getEscapeCharacter());

        // Test chaining withEscape calls
        CSVFormat chainedFormat = baseFormat.withEscape('a').withEscape('b');
        assertEquals(Character.valueOf('b'), chainedFormat.getEscapeCharacter());

        // Test withEscape on a format that already has an escape character
        CSVFormat formatWithEscape = baseFormat.withEscape('x');
        CSVFormat newFormat = formatWithEscape.withEscape('y');
        assertEquals(Character.valueOf('x'), formatWithEscape.getEscapeCharacter());
        assertEquals(Character.valueOf('y'), newFormat.getEscapeCharacter());
    }
}
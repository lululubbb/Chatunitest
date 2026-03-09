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

class CSVFormat_50_5Test {

    @Test
    @Timeout(8000)
    void testWithEscape_withEscapeChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat result = baseFormat.withEscape(escapeChar);

        assertNotNull(result);
        assertEquals(Character.valueOf(escapeChar), result.getEscapeCharacter());
        // Original should remain unchanged (immutability)
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
        assertNull(baseFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_withSameEscapeCharReturnsSameInstance() throws Exception {
        // Use withEscape(Character) to create baseFormat with escape 'a'
        CSVFormat baseFormat = CSVFormat.DEFAULT.withEscape(Character.valueOf('a'));
        CSVFormat result = baseFormat.withEscape('a');

        // The withEscape(Character) method returns same instance if escape character unchanged
        assertSame(baseFormat, result);
    }
}
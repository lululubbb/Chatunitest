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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatWithEscapeTest {

    @Test
    @Timeout(8000)
    void testWithEscape_primitiveChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char escapeChar = '\\';

        CSVFormat newFormat = baseFormat.withEscape(escapeChar);

        assertNotNull(newFormat);
        assertEquals(Character.valueOf(escapeChar), newFormat.getEscapeCharacter());
        // Original format unchanged
        assertNull(baseFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_Character_null() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Use reflection to access public withEscape(Character)
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        CSVFormat resultFormat = (CSVFormat) withEscapeCharMethod.invoke(baseFormat, (Object) null);

        assertNotNull(resultFormat);
        assertNull(resultFormat.getEscapeCharacter());
        // Original format unchanged
        assertNull(baseFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    void testWithEscape_Character_setEscape() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        Character escapeChar = '\\';

        // Use reflection to access public withEscape(Character)
        Method withEscapeCharMethod = CSVFormat.class.getMethod("withEscape", Character.class);

        CSVFormat resultFormat = (CSVFormat) withEscapeCharMethod.invoke(baseFormat, escapeChar);

        assertNotNull(resultFormat);
        assertEquals(escapeChar, resultFormat.getEscapeCharacter());
        // Original format unchanged
        assertNull(baseFormat.getEscapeCharacter());
    }
}
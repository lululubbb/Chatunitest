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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CSVFormatIsLineBreakTest {

    private static Method isLineBreakMethod;

    @BeforeAll
    static void setup() throws Exception {
        // The method takes primitive char, not Character
        isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithLF() throws Exception {
        // Pass primitive char directly
        boolean result = (boolean) isLineBreakMethod.invoke(null, '\n');
        assertTrue(result, "LF should be recognized as a line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithCR() throws Exception {
        boolean result = (boolean) isLineBreakMethod.invoke(null, '\r');
        assertTrue(result, "CR should be recognized as a line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakWithOtherChars() throws Exception {
        char[] nonLineBreakChars = { 'a', ' ', ',', '\t', '\\', '\"', '|' };
        for (char c : nonLineBreakChars) {
            boolean result = (boolean) isLineBreakMethod.invoke(null, c);
            assertFalse(result, "Character '" + c + "' should NOT be recognized as a line break");
        }
    }
}
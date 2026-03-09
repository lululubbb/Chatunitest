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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CSVFormatIsLineBreakTest {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    static void setUp() throws Exception {
        // Access private static method isLineBreak(char)
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);

        // Access private static method isLineBreak(Character)
        isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakChar_withLF() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\n');
        assertTrue(result, "LF (\\n) should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakChar_withCR() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\r');
        assertTrue(result, "CR (\\r) should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakChar_withOtherChar() throws Exception {
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, 'a');
        assertFalse(result, "'a' should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakChar_withNullChar() throws Exception {
        // char primitive cannot be null, no test needed
        // This test is to ensure no exception thrown on any char input
        boolean result = (boolean) isLineBreakCharMethod.invoke(null, '\0');
        assertFalse(result, "Null char '\\0' should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacter_withLF() throws Exception {
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\n'));
        assertTrue(result, "LF (\\n) Character should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacter_withCR() throws Exception {
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\r'));
        assertTrue(result, "CR (\\r) Character should be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacter_withOtherChar() throws Exception {
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('z'));
        assertFalse(result, "'z' Character should not be recognized as line break");
    }

    @Test
    @Timeout(8000)
    void testIsLineBreakCharacter_withNull() throws Exception {
        // invoke with single null argument requires passing (Object) null to avoid ambiguity
        Boolean result = (Boolean) isLineBreakCharacterMethod.invoke(null, (Object) null);
        assertFalse(result, "null Character should not be recognized as line break");
    }
}
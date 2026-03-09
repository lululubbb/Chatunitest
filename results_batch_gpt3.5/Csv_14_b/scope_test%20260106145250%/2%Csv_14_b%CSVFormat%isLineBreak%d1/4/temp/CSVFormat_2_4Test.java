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

public class CSVFormat_2_4Test {

    private static Method isLineBreakCharMethod;
    private static Method isLineBreakCharacterMethod;

    @BeforeAll
    public static void setUp() throws Exception {
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
        isLineBreakCharacterMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacterMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withCR() throws Exception {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withLF() throws Exception {
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar_withOtherChar() throws Exception {
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, ' '));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withNull() throws Exception {
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, (Object) null));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withCR() throws Exception {
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\r')));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withLF() throws Exception {
        assertTrue((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('\n')));
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter_withOtherChar() throws Exception {
        assertFalse((Boolean) isLineBreakCharacterMethod.invoke(null, Character.valueOf('x')));
    }
}
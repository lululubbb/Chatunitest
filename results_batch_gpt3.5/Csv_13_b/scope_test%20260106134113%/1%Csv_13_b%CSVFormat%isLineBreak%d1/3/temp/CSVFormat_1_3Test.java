package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
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

public class CSVFormat_1_3Test {

    private static Method isLineBreakCharMethod;

    @BeforeAll
    public static void setup() throws Exception {
        // The method takes a primitive char, not Character
        isLineBreakCharMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakCharMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakWithChar() throws Exception {
        // LF and CR should return true
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakCharMethod.invoke(null, '\r'));

        // Other chars should return false
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, ','));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, ' '));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, '\t'));
        assertFalse((Boolean) isLineBreakCharMethod.invoke(null, (char) 0));
    }
}
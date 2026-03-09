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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_1_2Test {

    @Test
    @Timeout(8000)
    public void testIsLineBreak() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        // LF (Line Feed) should return true
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));

        // CR (Carriage Return) should return true
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));

        // Other characters should return false
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ' '));
        assertFalse((Boolean) isLineBreakChar.invoke(null, '\t'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, '\\'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, '\"'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, '0'));
    }
}
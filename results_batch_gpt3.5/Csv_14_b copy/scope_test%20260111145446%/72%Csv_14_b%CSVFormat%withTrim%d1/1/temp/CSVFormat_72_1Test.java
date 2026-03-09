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
import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_72_1Test {

    @Test
    @Timeout(8000)
    public void testWithTrim() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withTrim();

        // Then
        assertTrue(result.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testWithTrimFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withTrim(false);

        // Then
        assertFalse(result.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar() throws Exception {
        // Given
        char c = '\n';

        // When
        boolean result = invokeIsLineBreakChar(c);

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() throws Exception {
        // Given
        Character c = '\r';

        // When
        boolean result = invokeIsLineBreakCharacter(c);

        // Then
        assertTrue(result);
    }

    // Helper method to invoke private isLineBreak(char) method using reflection
    private boolean invokeIsLineBreakChar(char c) throws Exception {
        CSVFormat csvFormat = new CSVFormat(',', '\"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        return (boolean) method.invoke(csvFormat, c);
    }

    // Helper method to invoke private isLineBreak(Character) method using reflection
    private boolean invokeIsLineBreakCharacter(Character c) throws Exception {
        CSVFormat csvFormat = new CSVFormat(',', '\"', null, null, null, false, true, "\r\n",
                null, null, null, false, false, false, false, false);
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        return (boolean) method.invoke(csvFormat, c);
    }
}
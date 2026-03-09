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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

public class CSVFormat_29_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscape_primitiveChar_callsWithEscapeCharacter() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get the private withEscape(Character) method and mock it
        Method withEscapeCharMethod = CSVFormat.class.getDeclaredMethod("withEscape", Character.class);
        withEscapeCharMethod.setAccessible(true);

        // Spy on the CSVFormat instance to verify withEscape(Character) call
        CSVFormat spyFormat = spy(format);

        // Call the focal method withEscape(char)
        char escapeChar = '\\';
        CSVFormat returnedFormat = spyFormat.withEscape(escapeChar);

        // Verify that withEscape(Character) was called with Character.valueOf(escapeChar)
        verify(spyFormat).withEscape(Character.valueOf(escapeChar));

        // Since withEscape(Character) is called on spyFormat, returnedFormat should be the return of that call
        // We cannot mock the return easily, so just check returnedFormat is not null
        assertNotNull(returnedFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_primitiveChar_nullChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get the private withEscape(Character) method and spy on CSVFormat
        CSVFormat spyFormat = spy(format);

        // Call withEscape with '\0' character (null char)
        char escapeChar = '\0';
        CSVFormat returnedFormat = spyFormat.withEscape(escapeChar);

        verify(spyFormat).withEscape(Character.valueOf(escapeChar));
        assertNotNull(returnedFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithEscape_primitiveChar_variousChars() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat spyFormat = spy(format);

        char[] testChars = {',', '\"', '\n', '\r', '\t', 'a', '1', ' '};
        for (char c : testChars) {
            CSVFormat returnedFormat = spyFormat.withEscape(c);
            verify(spyFormat).withEscape(Character.valueOf(c));
            assertNotNull(returnedFormat);
            // Clear invocations using Mockito.clearInvocations()
            Mockito.clearInvocations(spyFormat);
        }
    }
}
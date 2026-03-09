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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_47_1Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter_validDelimiter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        char delimiter = ';';

        CSVFormat newFormat = baseFormat.withDelimiter(delimiter);

        assertNotNull(newFormat);
        assertEquals(delimiter, newFormat.getDelimiter());
        // The original format should remain unchanged
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), baseFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiter_throwsException() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with '\n' LF
        IllegalArgumentException exLF = assertThrows(IllegalArgumentException.class, () -> {
            baseFormat.withDelimiter('\n');
        });
        assertEquals("The delimiter cannot be a line break", exLF.getMessage());

        // Test with '\r' CR
        IllegalArgumentException exCR = assertThrows(IllegalArgumentException.class, () -> {
            baseFormat.withDelimiter('\r');
        });
        assertEquals("The delimiter cannot be a line break", exCR.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakMethod() throws Exception {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));

        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Object) null));
    }
}
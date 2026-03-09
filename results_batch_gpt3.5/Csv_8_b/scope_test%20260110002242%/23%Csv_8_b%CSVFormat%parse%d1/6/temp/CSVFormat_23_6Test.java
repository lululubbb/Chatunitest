package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_23_6Test {

    private CSVFormat csvFormat;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    public void testParseReturnsCSVParserInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVParser parser = csvFormat.parse(mockReader);
        assertNotNull(parser);

        // Use reflection to get private fields 'in' and 'format' from CSVParser
        Field readerField = CSVParser.class.getDeclaredField("in");
        readerField.setAccessible(true);
        Reader parserReader = (Reader) readerField.get(parser);
        assertEquals(mockReader, parserReader);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parserFormat = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, parserFormat);
    }

    @Test
    @Timeout(8000)
    public void testParseThrowsIOException() throws IOException {
        Reader throwingReader = mock(Reader.class);
        doThrow(new IOException("read error")).when(throwingReader).read(any(char[].class), anyInt(), anyInt());

        // CSVParser constructor reads from Reader, so IOException can be thrown during parse
        assertThrows(IOException.class, () -> {
            csvFormat.parse(throwingReader);
        });
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakChar() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((Boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((Boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, 'a'));
        assertFalse((Boolean) isLineBreakChar.invoke(null, ','));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreakCharacter() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\n')));
        assertTrue((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('\r')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, Character.valueOf(',')));
        assertFalse((Boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }
}
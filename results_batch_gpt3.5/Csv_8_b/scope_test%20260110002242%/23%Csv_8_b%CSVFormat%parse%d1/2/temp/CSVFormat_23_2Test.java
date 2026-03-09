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
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_23_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testParse_withValidReader_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String csvData = "header1,header2\nvalue1,value2";
        Reader reader = new StringReader(csvData);

        CSVParser parser = csvFormat.parse(reader);

        assertNotNull(parser);

        // Use reflection to access private final field 'in' and 'format' in CSVParser
        Field readerField = CSVParser.class.getDeclaredField("in");
        readerField.setAccessible(true);
        Reader actualReader = (Reader) readerField.get(parser);
        assertSame(reader, actualReader);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, actualFormat);
    }

    @Test
    @Timeout(8000)
    public void testParse_withNullReader_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            csvFormat.parse(null);
        });
    }

    @Test
    @Timeout(8000)
    public void testParse_invokesPrivateMethodsUsingReflection() throws Exception {
        // Access private static method isLineBreak(char)
        Method isLineBreakChar = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakChar.setAccessible(true);

        assertTrue((boolean) isLineBreakChar.invoke(null, '\n'));
        assertTrue((boolean) isLineBreakChar.invoke(null, '\r'));
        assertFalse((boolean) isLineBreakChar.invoke(null, 'a'));

        // Access private static method isLineBreak(Character)
        Method isLineBreakCharacter = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        isLineBreakCharacter.setAccessible(true);

        assertTrue((boolean) isLineBreakCharacter.invoke(null, '\n'));
        assertTrue((boolean) isLineBreakCharacter.invoke(null, '\r'));
        assertFalse((boolean) isLineBreakCharacter.invoke(null, 'a'));
        assertFalse((boolean) isLineBreakCharacter.invoke(null, (Character) null));
    }

    @Test
    @Timeout(8000)
    public void testParse_withMockReader_invokesCSVParserConstructor() throws IOException, NoSuchFieldException, IllegalAccessException {
        Reader mockReader = mock(Reader.class);

        CSVParser parser = csvFormat.parse(mockReader);

        assertNotNull(parser);

        // Use reflection to access private final field 'in' and 'format' in CSVParser
        Field readerField = CSVParser.class.getDeclaredField("in");
        readerField.setAccessible(true);
        Reader actualReader = (Reader) readerField.get(parser);
        assertSame(mockReader, actualReader);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(csvFormat, actualFormat);
    }
}
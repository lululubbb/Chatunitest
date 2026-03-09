package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CSVParser_7_2Test {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Create CSVParser instance normally
        csvParser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);

        // Inject lexerMock into csvParser via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier if present (Java 12+ may not allow this; this works in older versions)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testGetCurrentLineNumber_returnsLexerCurrentLineNumber() {
        long expectedLineNumber = 42L;
        when(lexerMock.getCurrentLineNumber()).thenReturn(expectedLineNumber);

        long actualLineNumber = csvParser.getCurrentLineNumber();

        assertEquals(expectedLineNumber, actualLineNumber);
        verify(lexerMock).getCurrentLineNumber();
    }

    @Test
    @Timeout(8000)
    void testGetCurrentLineNumber_reflectionInvocation() throws Exception {
        long expectedLineNumber = 100L;
        when(lexerMock.getCurrentLineNumber()).thenReturn(expectedLineNumber);

        Method method = CSVParser.class.getDeclaredMethod("getCurrentLineNumber");
        method.setAccessible(true);
        Object result = method.invoke(csvParser);

        assertTrue(result instanceof Long);
        assertEquals(expectedLineNumber, ((Long) result).longValue());

        verify(lexerMock).getCurrentLineNumber();
    }
}
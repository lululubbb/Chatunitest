package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_13_6Test {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Create CSVParser instance with a dummy Reader and CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Use reflection to set the private final lexer field to the mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        // Set the lexer field to the mock
        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    public void testIsClosed_whenLexerIsClosed_returnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);

        boolean result = csvParser.isClosed();

        assertTrue(result);
        verify(lexerMock, times(1)).isClosed();
    }

    @Test
    @Timeout(8000)
    public void testIsClosed_whenLexerIsNotClosed_returnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);

        boolean result = csvParser.isClosed();

        assertFalse(result);
        verify(lexerMock, times(1)).isClosed();
    }
}
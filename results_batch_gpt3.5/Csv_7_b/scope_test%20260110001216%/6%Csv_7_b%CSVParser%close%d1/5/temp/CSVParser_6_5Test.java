package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.File;
import java.io.FileReader;
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

import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_6_5Test {

    private CSVParser parser;
    private Closeable lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a spy of CSVParser with a dummy Reader and CSVFormat to allow field injection
        parser = Mockito.spy(new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT));

        // Mock the lexer field as Closeable (Lexer implements Closeable)
        lexerMock = mock(Closeable.class);

        // Inject the mocked lexer into the parser using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void close_shouldCallLexerClose_whenLexerIsNotNull() throws IOException, NoSuchFieldException, IllegalAccessException {
        parser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_shouldNotThrow_whenLexerIsNull() throws Exception {
        // Set lexer to null using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier via reflection to allow setting null
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(parser, null);

        assertDoesNotThrow(() -> parser.close());
    }

    @Test
    @Timeout(8000)
    void close_shouldPropagateIOException() throws IOException {
        doThrow(new IOException("close failed")).when(lexerMock).close();
        IOException thrown = assertThrows(IOException.class, () -> parser.close());
        assertEquals("close failed", thrown.getMessage());
    }
}
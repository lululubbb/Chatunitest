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

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CSVParser_13_1Test {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a dummy CSVFormat (can be mocked or real as no usage here)
        CSVFormat format = mock(CSVFormat.class);

        // Create a dummy Reader since constructor requires it
        java.io.Reader reader = new java.io.StringReader("");

        // Instantiate CSVParser
        parser = new CSVParser(reader, format);

        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Use reflection to set the private final lexer field to the mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier via reflection if necessary
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        // Set the lexer field to the mock
        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsClosed_returnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);
        assertTrue(parser.isClosed());
        verify(lexerMock).isClosed();
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsNotClosed_returnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);
        assertFalse(parser.isClosed());
        verify(lexerMock).isClosed();
    }
}
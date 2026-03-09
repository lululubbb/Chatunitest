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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_13_4Test {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a CSVParser instance with a dummy Reader and CSVFormat
        csvParser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);

        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Use reflection to set the private final lexer field to the mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from the lexer field (if present)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        // Set the lexer field to the mock
        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsClosed_returnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);
        assertTrue(csvParser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsNotClosed_returnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);
        assertFalse(csvParser.isClosed());
    }
}
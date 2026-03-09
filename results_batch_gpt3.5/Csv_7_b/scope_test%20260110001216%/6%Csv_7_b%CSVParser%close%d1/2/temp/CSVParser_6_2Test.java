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
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_6_2Test {

    private CSVParser parser;
    private Closeable mockLexer;

    @BeforeEach
    void setUp() throws Exception {
        // Create a real CSVParser instance using a dummy CSVFormat and StringReader
        parser = new CSVParser(new java.io.StringReader("a,b,c\n1,2,3"), CSVFormat.DEFAULT);

        // Create a mock Lexer (implements Closeable)
        mockLexer = mock(Closeable.class);

        // Use reflection to set private final lexer field to mockLexer
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, mockLexer);
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNotNull_ClosesLexer() throws IOException {
        parser.close();
        // Verify lexer.close() was called once
        verify(mockLexer, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_LexerNull_NoException() throws Exception {
        // Set lexer to null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier from lexer field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);

        lexerField.set(parser, null);

        // Should not throw exception
        assertDoesNotThrow(() -> parser.close());
    }

    @Test
    @Timeout(8000)
    void testClose_LexerCloseThrowsIOException_Propagated() throws IOException {
        doThrow(new IOException("close failed")).when(mockLexer).close();
        IOException thrown = assertThrows(IOException.class, () -> parser.close());
        assertEquals("close failed", thrown.getMessage());
    }
}
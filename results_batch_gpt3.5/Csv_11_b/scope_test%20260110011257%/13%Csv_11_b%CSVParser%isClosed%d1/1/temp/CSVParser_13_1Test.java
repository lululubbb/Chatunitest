package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_13_1Test {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    public void setUp() throws Exception {
        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Create CSVParser instance with dummy constructor parameters
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Use reflection to set the private final lexer field to the mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier using reflection (for Java 12 and below)
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(lexerField, lexerField.getModifiers() & ~Modifier.FINAL);
        } catch (NoSuchFieldException e) {
            // For Java 12+, the 'modifiers' field is not present; ignore
        }

        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    public void testIsClosed_whenLexerIsClosed_returnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);

        boolean result = csvParser.isClosed();

        assertTrue(result);
        verify(lexerMock).isClosed();
    }

    @Test
    @Timeout(8000)
    public void testIsClosed_whenLexerIsNotClosed_returnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);

        boolean result = csvParser.isClosed();

        assertFalse(result);
        verify(lexerMock).isClosed();
    }
}
package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserIsClosedTest {

    private CSVParser parser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Create a mock Lexer
        lexerMock = mock(Lexer.class);

        // Create a real CSVParser instance with a dummy Reader
        parser = new CSVParser(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) {
                return -1; // empty reader
            }
            @Override
            public void close() throws IOException {
                // no-op
            }
        }, CSVFormat.DEFAULT);

        // Use reflection to replace the final lexer field with the mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(lexerField, lexerField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        lexerField.set(parser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);
        assertTrue(parser.isClosed());
        verify(lexerMock).isClosed();
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);
        assertFalse(parser.isClosed());
        verify(lexerMock).isClosed();
    }
}
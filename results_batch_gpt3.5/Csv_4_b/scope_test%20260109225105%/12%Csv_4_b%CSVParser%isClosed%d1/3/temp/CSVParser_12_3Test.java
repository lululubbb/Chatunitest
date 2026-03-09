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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_12_3Test {

    @Mock
    private Lexer lexerMock;

    private CSVParser csvParser;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        // Use a constructor with Reader and CSVFormat to instantiate CSVParser
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);
        // Inject the mocked lexer into csvParser using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, lexerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testIsClosed_WhenLexerIsClosed_ReturnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);
        assertTrue(csvParser.isClosed());
        verify(lexerMock).isClosed();
    }

    @Test
    @Timeout(8000)
    void testIsClosed_WhenLexerIsNotClosed_ReturnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);
        assertFalse(csvParser.isClosed());
        verify(lexerMock).isClosed();
    }
}
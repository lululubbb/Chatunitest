package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
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

import java.lang.reflect.Field;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Lexer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVParser_12_2Test {

    @Mock
    private Lexer lexerMock;

    private CSVParser csvParser;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);

        // Create CSVParser instance with a dummy Reader and CSVFormat using reflection since constructor is public
        csvParser = new CSVParser(new java.io.StringReader(""), null);

        // Inject mocked lexer into csvParser using reflection
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
    void testGetFirstEndOfLine_whenLexerReturnsEol() {
        String expectedEol = "\n";
        when(lexerMock.getFirstEol()).thenReturn(expectedEol);

        String actualEol = csvParser.getFirstEndOfLine();

        assertEquals(expectedEol, actualEol);
        verify(lexerMock).getFirstEol();
    }

    @Test
    @Timeout(8000)
    void testGetFirstEndOfLine_whenLexerReturnsNull() {
        when(lexerMock.getFirstEol()).thenReturn(null);

        String actualEol = csvParser.getFirstEndOfLine();

        assertNull(actualEol);
        verify(lexerMock).getFirstEol();
    }
}
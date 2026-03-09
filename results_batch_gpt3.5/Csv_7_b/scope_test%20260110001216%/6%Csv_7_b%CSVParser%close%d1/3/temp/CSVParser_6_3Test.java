package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
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

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVParserCloseTest {

    @Mock
    private Lexer lexerMock;

    private CSVParser csvParser;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);

        // Create real CSVParser instance via constructor with dummy Reader and format
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Inject lexerMock into csvParser via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void close_callsLexerClose_whenLexerNotNull() throws IOException {
        csvParser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_doesNotThrow_whenLexerIsNull() throws IOException, Exception {
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, null);
        csvParser.close();
        // no exception means pass
    }
}
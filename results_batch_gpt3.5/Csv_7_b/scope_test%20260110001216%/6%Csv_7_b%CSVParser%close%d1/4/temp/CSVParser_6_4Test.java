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

import org.junit.jupiter.api.AfterEach;
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
        // Use reflection to create CSVParser instance with mocked Lexer
        csvParser = spy(new CSVParser(new java.io.StringReader("a,b,c\n1,2,3"), CSVFormat.DEFAULT));
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
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
    void testClose_WithNonNullLexer_ClosesLexer() throws IOException {
        csvParser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_WithNullLexer_DoesNotThrow() throws IOException, NoSuchFieldException, IllegalAccessException {
        // Set lexer to null using reflection
        java.lang.reflect.Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, null);

        // Should not throw any exception
        csvParser.close();
    }
}
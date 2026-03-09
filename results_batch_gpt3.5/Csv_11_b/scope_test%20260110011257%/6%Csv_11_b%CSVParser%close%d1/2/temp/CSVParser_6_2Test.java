package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
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

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVParser_6_2Test {

    @Mock
    private Lexer lexerMock;

    private CSVParser csvParser;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);

        // Use reflection to find a constructor and create CSVParser instance
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        csvParser = constructor.newInstance(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Inject lexerMock into csvParser using reflection
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
    void testClose_lexerNotNull_callsLexerClose() throws IOException {
        csvParser.close();
        verify(lexerMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_lexerNull_noException() throws Exception {
        // Set lexer to null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, null);

        // Should not throw any exception
        assertDoesNotThrow(() -> csvParser.close());
    }
}
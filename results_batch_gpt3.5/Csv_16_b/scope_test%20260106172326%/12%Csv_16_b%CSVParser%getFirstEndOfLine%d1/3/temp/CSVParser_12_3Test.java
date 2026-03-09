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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

class CSVParser_12_3Test {

    @Mock
    private Lexer lexerMock;

    private CSVParser csvParser;

    private MockitoSession mockitoSession;

    @BeforeEach
    void setUp() throws Exception {
        mockitoSession = org.mockito.Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.LENIENT)
                .startMocking();

        // Create CSVParser instance with a dummy Reader and CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Inject the mocked Lexer into the csvParser instance using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, lexerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mockitoSession != null) {
            mockitoSession.finishMocking();
        }
    }

    @Test
    @Timeout(8000)
    void testGetFirstEndOfLine_returnsExpectedString() {
        String expectedEol = "\n";
        when(lexerMock.getFirstEol()).thenReturn(expectedEol);

        String actualEol = csvParser.getFirstEndOfLine();

        assertEquals(expectedEol, actualEol);
        verify(lexerMock, times(1)).getFirstEol();
    }

    @Test
    @Timeout(8000)
    void testGetFirstEndOfLine_returnsNull() {
        when(lexerMock.getFirstEol()).thenReturn(null);

        String actualEol = csvParser.getFirstEndOfLine();

        assertNull(actualEol);
        verify(lexerMock, times(1)).getFirstEol();
    }
}
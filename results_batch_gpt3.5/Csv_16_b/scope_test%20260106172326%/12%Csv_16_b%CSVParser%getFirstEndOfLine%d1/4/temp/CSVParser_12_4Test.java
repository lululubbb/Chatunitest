package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVParser_12_4Test {

    @Mock
    private Lexer lexerMock;

    private CSVParser csvParser;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.initMocks(this);

        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        csvParser = constructor.newInstance(new StringReader(""), CSVFormat.DEFAULT);

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
    void testGetFirstEndOfLine_ReturnsExpected() {
        String expectedEol = "\n";
        when(lexerMock.getFirstEol()).thenReturn(expectedEol);

        String actualEol = csvParser.getFirstEndOfLine();

        assertEquals(expectedEol, actualEol);
        verify(lexerMock).getFirstEol();
    }

    @Test
    @Timeout(8000)
    void testGetFirstEndOfLine_ReturnsNull() {
        when(lexerMock.getFirstEol()).thenReturn(null);

        String actualEol = csvParser.getFirstEndOfLine();

        assertNull(actualEol);
        verify(lexerMock).getFirstEol();
    }
}
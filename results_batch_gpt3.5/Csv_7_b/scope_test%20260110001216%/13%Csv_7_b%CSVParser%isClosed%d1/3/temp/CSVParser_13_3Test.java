package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_13_3Test {

    private CSVParser csvParser;
    private Lexer lexerMock;

    @BeforeEach
    void setUp() throws Exception {
        // Provide non-null Reader and CSVFormat to avoid NullPointerException in constructor
        csvParser = new CSVParser(new StringReader(""), CSVFormat.DEFAULT);
        lexerMock = mock(Lexer.class);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, lexerMock);
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);

        boolean result = csvParser.isClosed();

        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsClosedReturnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);

        boolean result = csvParser.isClosed();

        assertFalse(result);
    }
}
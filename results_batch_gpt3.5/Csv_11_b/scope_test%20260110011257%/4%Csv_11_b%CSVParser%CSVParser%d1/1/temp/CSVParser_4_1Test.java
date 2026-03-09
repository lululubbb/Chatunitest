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
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.Lexer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_1Test {

    private Reader mockReader;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullReader_ThrowsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, mockFormat);
        });
        assertEquals("reader", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullFormat_ThrowsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVParser(mockReader, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_ValidArguments_InitializesFields() throws Exception {
        // Create the CSVParser instance normally
        CSVParser parser = new CSVParser(mockReader, mockFormat);

        // Replace lexer field with a mocked Lexer
        Lexer mockedLexer = mock(Lexer.class);
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, mockedLexer);

        // Verify format field is set
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(parser);
        assertSame(mockFormat, formatValue);

        // Verify headerMap is initialized (not null)
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);

        // Verify lexer field is set to mockedLexer
        Lexer lexer = (Lexer) lexerField.get(parser);
        assertSame(mockedLexer, lexer);
    }
}
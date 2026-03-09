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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_2_2Test {

    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_NullString_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, mockFormat);
        });
        assertEquals("string", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse("some,string", null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidStringAndFormat_ReturnsCSVParser() throws IOException {
        String input = "a,b,c\n1,2,3\n4,5,6";

        // Since the CSVFormat is mocked, we need to stub methods that CSVParser constructor or lexer might call.
        // Stub toString() to avoid potential NPE or unexpected behavior
        when(mockFormat.toString()).thenReturn("mockFormat");

        CSVParser parser = CSVParser.parse(input, mockFormat);
        assertNotNull(parser);

        // Verify internal state by reflection
        try {
            Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            Object formatValue = formatField.get(parser);
            assertSame(mockFormat, formatValue);

            Field lexerField = CSVParser.class.getDeclaredField("lexer");
            lexerField.setAccessible(true);
            Object lexerValue = lexerField.get(parser);
            assertNotNull(lexerValue);

            Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
            recordNumberField.setAccessible(true);
            long recordNumber = recordNumberField.getLong(parser);
            assertEquals(0L, recordNumber);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection access failed: " + e.getMessage());
        }
    }
}
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVParser_2_5Test {

    @Test
    @Timeout(8000)
    void testParse_NullString_ThrowsException() {
        CSVFormat format = CSVFormat.DEFAULT; // use real CSVFormat instead of mock
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_NullFormat_ThrowsException() {
        String input = "a,b,c\n1,2,3";
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(input, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_ValidString_ReturnsCSVParser() throws IOException {
        String input = "a,b,c\n1,2,3";

        CSVParser parser = CSVParser.parse(input, CSVFormat.DEFAULT);
        assertNotNull(parser);
        // Validate internal state via reflection
        try {
            Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            Object formatValue = formatField.get(parser);
            assertNotNull(formatValue);

            Field lexerField = CSVParser.class.getDeclaredField("lexer");
            lexerField.setAccessible(true);
            Object lexerValue = lexerField.get(parser);
            assertNotNull(lexerValue);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection access failed: " + e.getMessage());
        }
    }
}
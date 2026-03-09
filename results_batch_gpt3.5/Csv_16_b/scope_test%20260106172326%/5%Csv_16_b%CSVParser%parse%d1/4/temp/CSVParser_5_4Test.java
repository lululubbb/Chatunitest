package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
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

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    @Test
    @Timeout(8000)
    void parse_NullString_ThrowsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        String input = "a,b,c";
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(input, null);
        });
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidStringAndFormat_ReturnsCSVParserInstance() throws IOException, NoSuchFieldException, IllegalAccessException {
        String input = "a,b,c\n1,2,3";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(input, format);

        assertNotNull(parser);
        assertFalse(parser.isClosed());

        // Use reflection to access the private final field 'format'
        var formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);

        assertEquals(format, actualFormat);
    }
}
package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVParser_2_2Test {

    @Test
    @Timeout(8000)
    void parse_WithValidStringAndFormat_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String input = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT; // Use a real instance instead of mock

        CSVParser parser = CSVParser.parse(input, format);

        assertNotNull(parser);
        assertFalse(parser.isClosed());

        // Access private field 'format' using reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
        assertEquals(format, actualFormat);
    }

    @Test
    @Timeout(8000)
    void parse_WithNullString_ThrowsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT; // Use a real instance instead of mock

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_WithNullFormat_ThrowsNullPointerException() {
        String input = "a,b,c";

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(input, null);
        });
        assertEquals("format", thrown.getMessage());
    }
}
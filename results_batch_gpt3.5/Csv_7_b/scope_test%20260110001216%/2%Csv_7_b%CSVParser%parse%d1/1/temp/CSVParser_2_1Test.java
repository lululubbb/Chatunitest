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

class CSVParser_2_1Test {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        String input = "header1,header2\nvalue1,value2";
        CSVParser parser = CSVParser.parse(input, format);
        assertNotNull(parser);
        try {
            Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            CSVFormat actualFormat = (CSVFormat) formatField.get(parser);
            assertSame(format, actualFormat);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_throwsException() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string must not be null", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsException() {
        String input = "a,b,c";
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            CSVParser.parse(input, null);
        });
        assertEquals("format must not be null", thrown.getMessage());
    }
}
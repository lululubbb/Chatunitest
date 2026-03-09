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

class CSVParser_2_1Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        String input = "a,b,c\n1,2,3";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(input, format);

        assertNotNull(parser);

        // Access private field 'format' via reflection
        try {
            Field formatField = CSVParser.class.getDeclaredField("format");
            formatField.setAccessible(true);
            assertSame(format, formatField.get(parser));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
        }

        assertFalse(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_throwsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(null, format));
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsNullPointerException() {
        String input = "a,b,c";

        NullPointerException thrown = assertThrows(NullPointerException.class,
                () -> CSVParser.parse(input, null));
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_internalConstructorCalled() throws Exception {
        String input = "x,y,z";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(input, format);

        // Using reflection to verify private fields set correctly
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        assertEquals(0L, recordNumberField.getLong(parser));
    }
}
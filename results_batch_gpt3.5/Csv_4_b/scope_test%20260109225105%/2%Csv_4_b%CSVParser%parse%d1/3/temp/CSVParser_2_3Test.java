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

class CSVParser_2_3Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        String input = "a,b,c\n1,2,3";
        CSVFormat realFormat = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(input, realFormat);

        assertNotNull(parser);
        assertEquals(realFormat, getField(parser, "format"));
        assertFalse(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_throwsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;

        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((String) null, format));
        assertEquals("string", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsNullPointerException() {
        String input = "a,b,c";

        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(input, null));
        assertEquals("format", ex.getMessage());
    }

    private Object getField(Object instance, String fieldName) {
        try {
            Field field = instance.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            fail("Reflection failed for field: " + fieldName + " with exception: " + e);
            return null;
        }
    }
}
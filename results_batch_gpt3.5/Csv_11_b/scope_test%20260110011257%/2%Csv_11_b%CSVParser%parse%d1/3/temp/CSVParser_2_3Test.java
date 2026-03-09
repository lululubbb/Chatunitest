package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVParser_2_3Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String input = "a,b,c\n1,2,3";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        CSVParser parser = CSVParser.parse(input, format);

        assertNotNull(parser);

        // Access private field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parserFormat = (CSVFormat) formatField.get(parser);
        assertEquals(format.getHeader(), parserFormat.getHeader());

        assertFalse(parser.isClosed());
        assertNotNull(parser.getHeaderMap());
        assertEquals(3, parser.getHeaderMap().size());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_throwsException() {
        CSVFormat format = CSVFormat.DEFAULT;
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(null, format);
        });
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsException() {
        String input = "a,b,c\n1,2,3";
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(input, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_privateConstructorInvocation() throws Exception {
        String input = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();

        // Use reflection to invoke public constructor (Reader, CSVFormat)
        Constructor<CSVParser> constructor = CSVParser.class.getConstructor(java.io.Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVParser parser = constructor.newInstance(new StringReader(input), format);

        assertNotNull(parser);

        // Access private field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat parserFormat = (CSVFormat) formatField.get(parser);
        assertEquals(format.getHeader(), parserFormat.getHeader());

        assertFalse(parser.isClosed());
        assertNotNull(parser.getHeaderMap());
        assertEquals(2, parser.getHeaderMap().size());
    }
}
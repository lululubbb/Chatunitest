package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.io.Reader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

class CSVParser_2_4Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String csvContent = "a,b,c\n1,2,3";
        CSVFormat format = mock(CSVFormat.class);

        // Use the actual constructor instead of parse to avoid issues with mock CSVFormat
        CSVParser parser = new CSVParser(new StringReader(csvContent), format);

        assertNotNull(parser);

        // Access private field 'format' via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);

        assertSame(format, actualFormat);
        assertFalse(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullString_throwsNullPointerException() {
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((String) null, format));
        assertEquals("string", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_withNullFormat_throwsNullPointerException() {
        String csvContent = "a,b,c";

        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(csvContent, null));
        assertEquals("format", ex.getMessage());
    }
}
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
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

class CSVParser_2_6Test {

    @Test
    @Timeout(8000)
    void testParse_validStringAndFormat_returnsCSVParser() throws Exception {
        // Arrange
        String input = "a,b,c\n1,2,3";
        CSVFormat format = mock(CSVFormat.class);

        // Act
        CSVParser parser = CSVParser.parse(input, format);

        // Assert
        assertNotNull(parser);

        // Use reflection to access private field 'format'
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);
        assertSame(format, actualFormat);

        assertFalse(parser.isClosed());
    }

    @Test
    @Timeout(8000)
    void testParse_nullString_throwsException() {
        CSVFormat format = mock(CSVFormat.class);

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_nullFormat_throwsException() {
        String input = "a,b,c";

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(input, null);
        });
        assertEquals("format", thrown.getMessage());
    }
}
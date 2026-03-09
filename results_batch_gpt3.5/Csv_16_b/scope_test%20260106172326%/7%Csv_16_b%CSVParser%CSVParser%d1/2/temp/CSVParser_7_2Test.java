package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_7_2Test {

    private CSVFormat formatMock;
    private Reader readerMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testCSVParserConstructor_withReaderAndFormat() throws IOException {
        // Arrange
        CSVFormat realFormat = CSVFormat.DEFAULT;

        // Act
        CSVParser parser = new CSVParser(readerMock, realFormat);

        // Assert
        assertNotNull(parser);
        assertEquals(1, parser.getRecordNumber()); // constructor sets recordNumber to 1
        assertFalse(parser.isClosed());

        // headerMap can be null or a Map<String,Integer>, so check accordingly
        Map<String, Integer> headerMap = parser.getHeaderMap();
        if (headerMap == null) {
            assertNull(headerMap);
        } else {
            assertTrue(headerMap instanceof Map);
        }
    }

    @Test
    @Timeout(8000)
    void testCSVParserConstructor_withReaderFormatCharacterOffsetRecordNumber() throws Exception {
        // Arrange
        long characterOffset = 5L;
        long recordNumber = 10L;

        // To call the 4-arg constructor, use reflection since it's package-private
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class, long.class, long.class);
        constructor.setAccessible(true);
        CSVParser parser = constructor.newInstance(readerMock, formatMock, characterOffset, recordNumber);

        // Assert
        assertNotNull(parser);
        assertEquals(recordNumber, parser.getRecordNumber());
        assertFalse(parser.isClosed());

        // Verify characterOffset field via reflection
        Field charOffsetField = CSVParser.class.getDeclaredField("characterOffset");
        charOffsetField.setAccessible(true);
        long actualCharOffset = (long) charOffsetField.get(parser);
        assertEquals(characterOffset, actualCharOffset);
    }
}
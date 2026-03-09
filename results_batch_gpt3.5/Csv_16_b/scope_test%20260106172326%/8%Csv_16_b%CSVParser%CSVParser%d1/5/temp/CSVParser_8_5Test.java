package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_8_5Test {

    private CSVFormat format;

    @BeforeEach
    public void setup() {
        format = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testConstructor_withValidReaderAndFormat() throws Exception {
        String csvData = "a,b,c\n1,2,3";
        Reader reader = new StringReader(csvData);

        CSVParser parser = new CSVParser(reader, format, 10L, 5L);

        // Verify fields set correctly via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(format, formatField.get(parser));

        Field characterOffsetField = CSVParser.class.getDeclaredField("characterOffset");
        characterOffsetField.setAccessible(true);
        assertEquals(10L, characterOffsetField.getLong(parser));

        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        // recordNumber is set to recordNumber - 1, so expect 4L
        assertEquals(4L, recordNumberField.getLong(parser));

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Map<?, ?> headerMap = (Map<?, ?>) headerMapField.get(parser);
        assertNotNull(headerMap);
        // Default CSVFormat.DEFAULT has no header, so headerMap should be empty
        assertTrue(headerMap.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullReader_throwsException() {
        assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, format, 0L, 0L);
        });
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullFormat_throwsException() {
        Reader reader = new StringReader("a,b,c");
        assertThrows(NullPointerException.class, () -> {
            new CSVParser(reader, null, 0L, 0L);
        });
    }

}
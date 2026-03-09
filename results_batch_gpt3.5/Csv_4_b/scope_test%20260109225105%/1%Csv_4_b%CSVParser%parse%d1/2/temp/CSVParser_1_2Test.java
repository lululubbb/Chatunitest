package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseFileTest {

    private CSVFormat format;

    @BeforeEach
    void setup() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_nullFile_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, format);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_throwsNullPointerException() {
        File file = mock(File.class);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(file, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_validFile_createsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        File file = mock(File.class);

        // Mock file's toPath() and toString() to avoid issues inside FileReader or CSVParser
        when(file.toString()).thenReturn("dummy.csv");

        CSVParser parser = CSVParser.parse(file, format);

        assertNotNull(parser);
        assertEquals(format, getField(parser, "format"));

        Object recordField = getField(parser, "record");
        assertTrue(recordField instanceof List);

        @SuppressWarnings("unchecked")
        List<?> recordList = (List<?>) recordField;
        assertNotNull(recordList);
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
    }
}
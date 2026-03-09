package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
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

import java.io.File;
import java.io.IOException;
import java.io.FileReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParserParseTest {

    private CSVFormat formatMock;
    private File fileMock;

    @BeforeEach
    void setup() {
        formatMock = mock(CSVFormat.class);
        fileMock = mock(File.class);
    }

    @Test
    @Timeout(8000)
    void parse_withValidFileAndFormat_returnsCSVParser() throws IOException {
        // Mock FileReader construction to avoid FileNotFoundException or actual file IO
        try (var mockedStaticFileReader = mockStatic(FileReader.class)) {
            FileReader fileReaderMock = mock(FileReader.class);
            mockedStaticFileReader.when(() -> new FileReader(fileMock)).thenReturn(fileReaderMock);

            CSVParser parser = CSVParser.parse(fileMock, formatMock);

            assertNotNull(parser);
            assertEquals(formatMock, getField(parser, "format"));
            assertNotNull(getField(parser, "headerMap"));
            assertNotNull(getField(parser, "lexer"));
            assertEquals(0L, getField(parser, "recordNumber"));
            assertNotNull(getField(parser, "record"));
            assertNotNull(getField(parser, "reusableToken"));
        }
    }

    @Test
    @Timeout(8000)
    void parse_withNullFile_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse((File) null, formatMock));
        assertEquals("file", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_withNullFormat_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> CSVParser.parse(fileMock, null));
        assertEquals("format", ex.getMessage());
    }

    @SuppressWarnings("unchecked")
    private <T> T getField(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = CSVParser.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
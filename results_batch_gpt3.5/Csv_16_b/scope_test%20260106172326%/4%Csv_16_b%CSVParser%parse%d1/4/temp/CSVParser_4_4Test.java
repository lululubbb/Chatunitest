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
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_4Test {

    private CSVFormat formatMock;
    private Reader readerMock;

    @BeforeEach
    void setup() {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testParse_WithValidReaderAndFormat_ShouldReturnCSVParserInstance() throws IOException {
        when(formatMock.getHeader()).thenReturn(null);
        CSVParser parser = CSVParser.parse(readerMock, formatMock);
        assertNotNull(parser);
        assertFalse(parser.isClosed());
        assertEquals(0L, parser.getRecordNumber());
        assertNotNull(parser.getHeaderMap());
    }

    @Test
    @Timeout(8000)
    void testParse_WithNullReader_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> CSVParser.parse((Reader) null, formatMock));
    }

    @Test
    @Timeout(8000)
    void testParse_WithNullFormat_ShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> CSVParser.parse(readerMock, null));
    }

    @Test
    @Timeout(8000)
    void testParse_PrivateConstructorInvocation() throws Exception {
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        when(formatMock.getHeader()).thenReturn(null);
        CSVParser parser = constructor.newInstance(readerMock, formatMock);
        assertNotNull(parser);
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testAddRecordValueViaReflection() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);
        CSVParser parser = CSVParser.parse(readerMock, formatMock);
        Method method = CSVParser.class.getDeclaredMethod("addRecordValue", boolean.class);
        method.setAccessible(true);
        method.invoke(parser, true);
    }

    @Test
    @Timeout(8000)
    void testInitializeHeaderViaReflection() throws Exception {
        when(formatMock.getHeader()).thenReturn(null);
        CSVParser parser = CSVParser.parse(readerMock, formatMock);
        Method method = CSVParser.class.getDeclaredMethod("initializeHeader");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) method.invoke(parser);
        assertNotNull(headerMap);
    }

}
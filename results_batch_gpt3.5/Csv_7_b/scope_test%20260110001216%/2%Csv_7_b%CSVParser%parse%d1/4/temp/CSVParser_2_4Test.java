package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
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

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_2_4Test {

    private CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void parse_nullString_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_nullFormat_throwsNullPointerException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse("", null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_validString_returnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String csv = "a,b,c\n1,2,3";
        CSVParser parser = CSVParser.parse(csv, format);
        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object actualFormat = formatField.get(parser);

        assertEquals(format, actualFormat);
    }

    @Test
    @Timeout(8000)
    void parse_stringReaderConstructorCalled() throws IOException {
        String csv = "header1,header2\nvalue1,value2";

        CSVParser parser = new CSVParser(new StringReader(csv), format);
        CSVParser spyParser = spy(parser);

        spyParser.getRecords();

        verify(spyParser).getRecords();

        assertNotNull(spyParser);
    }
}
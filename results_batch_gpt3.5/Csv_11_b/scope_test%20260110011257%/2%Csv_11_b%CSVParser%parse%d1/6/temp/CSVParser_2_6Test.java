package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
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

import org.junit.jupiter.api.Test;

class CSVParser_2_6Test {

    @Test
    @Timeout(8000)
    void parse_NullString_ThrowsNullPointerException() {
        CSVFormat format = mock(CSVFormat.class);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse("some,string", null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidStringAndFormat_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String input = "a,b,c\n1,2,3";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(input, format);

        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);

        assertEquals(format, actualFormat);
        assertFalse(parser.isClosed());
        assertEquals(0, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void parse_EmptyString_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        String input = "";
        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(input, format);

        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat actualFormat = (CSVFormat) formatField.get(parser);

        assertEquals(format, actualFormat);
    }
}
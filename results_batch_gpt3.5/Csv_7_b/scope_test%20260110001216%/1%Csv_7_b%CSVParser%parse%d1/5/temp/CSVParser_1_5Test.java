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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.Test;

class CSVParserParseFileTest {

    @Test
    @Timeout(8000)
    void parse_NullFile_ThrowsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((File) null, CSVFormat.DEFAULT);
        });
        assertEquals("file", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_NullFormat_ThrowsNullPointerException() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(tempFile, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void parse_ValidFileAndFormat_ReturnsCSVParser() throws IOException, NoSuchFieldException, IllegalAccessException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        CSVFormat format = CSVFormat.DEFAULT;

        CSVParser parser = CSVParser.parse(tempFile, format);

        assertNotNull(parser);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        Object formatValue = formatField.get(parser);
        assertSame(format, formatValue);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Object headerMapValue = headerMapField.get(parser);
        assertNotNull(headerMapValue);
        assertTrue(headerMapValue instanceof Map);
    }
}
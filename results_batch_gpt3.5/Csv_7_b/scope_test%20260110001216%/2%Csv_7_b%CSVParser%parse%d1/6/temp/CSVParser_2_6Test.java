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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class CSVParser_2_6Test {

    @Test
    @Timeout(8000)
    void testParse_withValidStringAndFormat_returnsCSVParser() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        String csvContent = "header1,header2\nvalue1,value2";

        CSVParser parser = CSVParser.parse(csvContent, format);
        assertNotNull(parser);
    }

    @Test
    @Timeout(8000)
    void testParse_nullString_throwsNullPointerException() {
        CSVFormat format = CSVFormat.DEFAULT;
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse((String) null, format);
        });
        assertEquals("string", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testParse_nullFormat_throwsNullPointerException() {
        String csvContent = "a,b";
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            CSVParser.parse(csvContent, (CSVFormat) null);
        });
        assertEquals("format", exception.getMessage());
    }
}
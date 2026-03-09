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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_9_1Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws IOException {
        String csvContent = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        csvParser = new CSVParser(new StringReader(csvContent), format);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_initialValue() {
        assertEquals(0L, csvParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_afterIncrement() throws Exception {
        // Use reflection to set the private field recordNumber
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.set(csvParser, 5L);

        assertEquals(5L, csvParser.getRecordNumber());
    }
}
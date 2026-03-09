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

public class CSVParser_9_5Test {

    private CSVParser csvParser;

    @BeforeEach
    public void setUp() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT;
        csvParser = new CSVParser(new StringReader("a,b,c\n1,2,3"), format);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumberInitial() throws Exception {
        // Use reflection to set recordNumber to 0 explicitly
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.set(csvParser, 0L);

        assertEquals(0L, csvParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumberAfterReadingRecords() throws Exception {
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);

        // simulate reading first record
        recordNumberField.set(csvParser, 1L);
        assertEquals(1L, csvParser.getRecordNumber());

        // simulate reading second record
        recordNumberField.set(csvParser, 2L);
        assertEquals(2L, csvParser.getRecordNumber());
    }
}
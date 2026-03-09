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

public class CSVParser_9_3Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws IOException {
        String csvData = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader();
        parser = new CSVParser(new StringReader(csvData), format);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_initialValue() throws Exception {
        // Use reflection to set recordNumber to 0
        setRecordNumber(parser, 0L);
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_afterNextRecord() throws IOException {
        parser.nextRecord();
        assertEquals(1L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_afterMultipleNextRecords() throws IOException {
        parser.nextRecord();
        parser.nextRecord();
        assertEquals(2L, parser.getRecordNumber());
    }

    private void setRecordNumber(CSVParser parser, long value) throws Exception {
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, value);
    }
}
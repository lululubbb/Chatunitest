package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_9_4Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        String csvData = "header1,header2\nvalue1,value2";
        CSVFormat format = CSVFormat.DEFAULT.withHeader("header1", "header2").withSkipHeaderRecord(true);
        parser = new CSVParser(new StringReader(csvData), format);
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber_initialValue() throws Exception {
        // Initially, recordNumber should be 0 before any records are read
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber_afterNextRecord() throws Exception {
        // Use nextRecord() to advance parser and increment recordNumber
        CSVRecord record = parser.nextRecord();
        assertEquals(1L, parser.getRecordNumber());

        // nextRecord() returns null if no more records
        assertNull(parser.nextRecord());
        assertEquals(1L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testSetRecordNumberUsingReflection() throws Exception {
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(parser, 42L);
        assertEquals(42L, parser.getRecordNumber());
    }
}
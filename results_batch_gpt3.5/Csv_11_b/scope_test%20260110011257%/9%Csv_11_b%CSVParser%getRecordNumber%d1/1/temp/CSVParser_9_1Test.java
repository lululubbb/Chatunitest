package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
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
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Field;

class CSVParser_9_1Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        String csvData = "header1,header2\nvalue1,value2";
        parser = new CSVParser(new StringReader(csvData), CSVFormat.DEFAULT.withHeader());
        // Using reflection to set recordNumber to a known value for testing
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        // recordNumber is a primitive long, use setLong
        recordNumberField.setLong(parser, 5L);
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber() {
        long recordNumber = parser.getRecordNumber();
        assertEquals(5L, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumberDefaultZero() throws Exception {
        CSVParser parserDefault = new CSVParser(new StringReader("a,b\n1,2"), CSVFormat.DEFAULT.withHeader());
        // No need to set recordNumber, default should be 0
        long recordNumber = parserDefault.getRecordNumber();
        assertEquals(0L, recordNumber);
    }
}
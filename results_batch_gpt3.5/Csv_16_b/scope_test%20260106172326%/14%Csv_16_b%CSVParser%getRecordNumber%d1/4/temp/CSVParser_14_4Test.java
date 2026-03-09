package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

public class CSVParser_14_4Test {

    private CSVParser parser;

    @BeforeEach
    public void setUp() throws IOException {
        String csvData = "header1,header2\nvalue1,value2\nvalue3,value4";
        parser = CSVParser.parse(new StringReader(csvData), CSVFormat.DEFAULT.withFirstRecordAsHeader());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_initialValue() {
        // Initially, recordNumber should be 0 as no records are read yet
        assertEquals(0L, parser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    public void testGetRecordNumber_afterReadingRecords() {
        // Iterate through records to increase recordNumber
        parser.iterator().forEachRemaining(r -> { /* consume all records */ });
        // After reading all records, recordNumber should equal number of records read
        assertEquals(2L, parser.getRecordNumber());
    }
}
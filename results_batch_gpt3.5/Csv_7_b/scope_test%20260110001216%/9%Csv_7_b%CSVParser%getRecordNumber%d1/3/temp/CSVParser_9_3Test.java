package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;

class CSVParser_9_3Test {

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws Exception {
        Reader reader = new StringReader("");
        CSVFormat format = CSVFormat.DEFAULT;
        csvParser = new CSVParser(reader, format);
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumberInitialValue() {
        assertEquals(0L, csvParser.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumberAfterModification() throws Exception {
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        recordNumberField.setLong(csvParser, 42L);

        assertEquals(42L, csvParser.getRecordNumber());
    }
}
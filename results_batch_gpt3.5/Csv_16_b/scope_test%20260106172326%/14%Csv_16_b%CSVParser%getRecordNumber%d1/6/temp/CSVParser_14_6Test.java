package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import java.io.StringReader;
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_14_6Test {

    private CSVParser parser;

    @BeforeEach
    void setUp() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Constructor<CSVParser> ctor = CSVParser.class.getDeclaredConstructor(
                java.io.Reader.class, CSVFormat.class, Long.TYPE, Long.TYPE);
        ctor.setAccessible(true);
        parser = ctor.newInstance(new StringReader("a,b,c\n1,2,3"), format, 0L, 42L);
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber() {
        assertEquals(42L, parser.getRecordNumber());
    }
}
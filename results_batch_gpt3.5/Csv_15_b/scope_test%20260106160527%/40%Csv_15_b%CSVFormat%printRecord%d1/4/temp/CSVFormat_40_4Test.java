package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, "value1", "value2", "value3");
        String result = out.toString();
        // It should print the three values separated properly and end with record separator CRLF
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, "singleValue");
        String result = out.toString();
        assertTrue(result.startsWith("singleValue"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out);
        String result = out.toString();
        // Should just print the record separator (newline)
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintMethodForEachValue() throws Exception {
        CSVFormat spyFormat = spy(csvFormat);
        StringBuilder out = new StringBuilder();
        Object[] values = new Object[] {"a", "b", "c"};

        // call with varargs, so expand values
        spyFormat.printRecord(out, values[0], values[1], values[2]);

        for (int i = 0; i < values.length; i++) {
            verify(spyFormat).print(values[i], out, i == 0);
        }
        verify(spyFormat).println(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvoke() throws Exception {
        StringBuilder out = new StringBuilder();
        Method method = CSVFormat.class.getDeclaredMethod("printRecord", Appendable.class, Object[].class);
        method.setAccessible(true);
        // Correctly wrap the Object[] to avoid varargs ambiguity
        method.invoke(csvFormat, out, new Object[] { "r1", "r2" });
        String result = out.toString();
        assertTrue(result.contains("r1"));
        assertTrue(result.contains("r2"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }
}
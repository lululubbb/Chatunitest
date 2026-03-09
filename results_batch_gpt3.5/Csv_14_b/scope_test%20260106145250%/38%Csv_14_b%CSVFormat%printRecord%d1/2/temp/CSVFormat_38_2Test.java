package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
    void testPrintRecordWithMultipleValues() throws IOException {
        StringBuilder out = new StringBuilder();
        Object[] values = {"value1", "value2", "value3"};

        csvFormat.printRecord(out, values);

        // The output should contain all values printed, separated properly and end with record separator CRLF
        String output = out.toString();
        assertTrue(output.startsWith("value1"));
        assertTrue(output.contains("value2"));
        assertTrue(output.contains("value3"));
        assertTrue(output.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithEmptyValues() throws IOException {
        StringBuilder out = new StringBuilder();
        Object[] values = {};

        csvFormat.printRecord(out, values);

        // It should just print a record separator
        assertEquals(csvFormat.getRecordSeparator(), out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNullValues() throws IOException {
        StringBuilder out = new StringBuilder();
        Object[] values = {null, "test", null};

        csvFormat.printRecord(out, values);

        String output = out.toString();
        // null values should be printed as empty or nullString if set, default is null so empty string
        assertTrue(output.startsWith(""));
        assertTrue(output.contains("test"));
        assertTrue(output.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithSingleValue() throws IOException {
        StringBuilder out = new StringBuilder();
        Object[] values = {"single"};

        csvFormat.printRecord(out, values);

        String output = out.toString();
        assertEquals("single" + csvFormat.getRecordSeparator(), output);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordInvokesPrintAndPrintln() throws Exception {
        // Spy the CSVFormat instance
        CSVFormat spyFormat = spy(csvFormat);
        StringBuilder out = new StringBuilder();
        Object[] values = {"val1", "val2"};

        // Use reflection to get the printRecord method to avoid signature issues with varargs and spying
        Method printRecordMethod = CSVFormat.class.getMethod("printRecord", Appendable.class, Object[].class);

        // Wrap values inside an Object array as varargs require
        Object[] args = new Object[] { out, values };
        printRecordMethod.invoke(spyFormat, args);

        // Verify print called for each value with correct newRecord flag
        verify(spyFormat, times(1)).print("val1", out, true);
        verify(spyFormat, times(1)).print("val2", out, false);
        // Verify println called once
        verify(spyFormat, times(1)).println(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordUsingReflection() throws Exception {
        StringBuilder out = new StringBuilder();
        Object[] values = {"reflect1", "reflect2"};

        Method printRecordMethod = CSVFormat.class.getMethod("printRecord", Appendable.class, Object[].class);

        // Wrap values inside an Object array as varargs require
        Object[] args = new Object[] { out, values };
        printRecordMethod.invoke(csvFormat, args);

        String output = out.toString();
        assertTrue(output.startsWith("reflect1"));
        assertTrue(output.contains("reflect2"));
        assertTrue(output.endsWith(csvFormat.getRecordSeparator()));
    }
}
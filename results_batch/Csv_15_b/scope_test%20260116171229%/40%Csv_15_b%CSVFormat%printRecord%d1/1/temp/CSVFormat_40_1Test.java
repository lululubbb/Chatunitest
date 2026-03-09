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
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "value1", "value2", "value3");
        String result = out.toString();
        // The record separator is CRLF by default
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "singleValue");
        String result = out.toString();
        assertTrue(result.startsWith("singleValue"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out);
        String result = out.toString();
        // Should just print a line separator
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValue() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, (Object) null);
        String result = out.toString();
        // null value should be printed as "null"
        assertTrue(result.startsWith("null"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintInvocation() throws Exception {
        Appendable out = new StringBuilder();
        Object[] values = new Object[] {"a", "b"};
        // The print method signature is print(Object, Appendable, boolean)
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // invoke print for first value with newRecord = true
        printMethod.invoke(csvFormat, values[0], out, true);
        // invoke print for second value with newRecord = false
        printMethod.invoke(csvFormat, values[1], out, false);

        // invoke println method to add record separator
        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);
        printlnMethod.invoke(csvFormat, out);

        // invoke printRecord normally, to compare output
        Appendable out2 = new StringBuilder();
        csvFormat.printRecord(out2, values);

        assertEquals(out2.toString(), out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMixedTypes() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "string", 123, 45.6, true, null);
        String result = out.toString();
        assertTrue(result.contains("string"));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("45.6"));
        assertTrue(result.contains("true"));
        assertTrue(result.contains("null"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyStringValue() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "");
        String result = out.toString();
        assertTrue(result.startsWith(""));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withAppendableThrowingIOException() throws IOException {
        Appendable out = mock(Appendable.class);
        doThrow(new IOException("test exception")).when(out).append(any(CharSequence.class));
        IOException thrown = assertThrows(IOException.class, () -> {
            csvFormat.printRecord(out, "value");
        });
        assertEquals("test exception", thrown.getMessage());
    }
}
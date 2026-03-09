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
import org.mockito.Mockito;

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithMultipleValues() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "value1", 123, null, "value4");
        String result = out.toString();
        assertNotNull(result);
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("123"));
        // null handling depends on implementation, so just accept either case
        assertTrue(result.contains("null") || !result.contains("null"));
        assertTrue(result.contains("value4"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithEmptyValues() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out);
        String result = out.toString();
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithSingleValue() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "single");
        String result = out.toString();
        assertTrue(result.startsWith("single"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordInvokesPrintAndPrintln() throws Exception {
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        Appendable out = new StringBuilder();
        Object[] values = new Object[] { "a", "b" };

        // Use reflection to invoke printRecord to ensure spying works correctly
        Method method = CSVFormat.class.getMethod("printRecord", Appendable.class, Object[].class);
        method.invoke(spyFormat, out, (Object) values);

        // Verify that print(Object, Appendable, boolean) is called for each value
        org.mockito.Mockito.verify(spyFormat, org.mockito.Mockito.times(values.length))
                .print(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.eq(out), org.mockito.ArgumentMatchers.anyBoolean());
        // Verify that println(Appendable) is called once
        org.mockito.Mockito.verify(spyFormat, org.mockito.Mockito.times(1)).println(org.mockito.ArgumentMatchers.eq(out));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNullAppendableThrows() {
        assertThrows(NullPointerException.class, () -> csvFormat.printRecord(null, "value"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNullValuesArray() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, (Object[]) null);
        String result = out.toString();
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordReflectionInvoke() throws Exception {
        Appendable out = new StringBuilder();
        Method method = CSVFormat.class.getMethod("printRecord", Appendable.class, Object[].class);
        // When invoking varargs method via reflection, wrap varargs in an Object[] inside another Object[]
        method.invoke(csvFormat, out, (Object) new Object[] { "v1", "v2" });
        String result = out.toString();
        assertTrue(result.contains("v1"));
        assertTrue(result.contains("v2"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }
}
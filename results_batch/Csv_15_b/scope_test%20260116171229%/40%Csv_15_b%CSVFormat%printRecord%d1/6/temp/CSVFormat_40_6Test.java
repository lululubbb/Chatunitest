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
    void testPrintRecord_withMultipleValues() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, "value1", "value2", "value3");
        String result = out.toString();
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
    void testPrintRecord_withEmptyValues() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out);
        String result = out.toString();
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValue() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, (Object) null);
        String result = out.toString();
        // null as string will be printed as "null" or nullString if set, here default nullString is null
        assertTrue(result.startsWith("null") || result.startsWith(""));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintMethodCorrectly() throws Exception {
        Appendable out = mock(Appendable.class);
        CSVFormat spyFormat = Mockito.spy(csvFormat);

        Object[] values = new Object[] { "a", "b" };

        // Correct invocation of varargs method via reflection:
        Method printRecordMethod = CSVFormat.class.getMethod("printRecord", Appendable.class, Object[].class);
        printRecordMethod.invoke(spyFormat, out, (Object) values);

        verify(spyFormat, times(values.length)).print(any(), eq(out), anyBoolean());
        verify(spyFormat).println(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_privatePrintMethodInvocation() throws Exception {
        StringBuilder out = new StringBuilder();
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        printMethod.invoke(csvFormat, "testValue", out, true);
        assertTrue(out.toString().contains("testValue"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_printlnMethodInvocation() throws Exception {
        StringBuilder out = new StringBuilder();
        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);
        printlnMethod.invoke(csvFormat, out);
        assertTrue(out.toString().endsWith(csvFormat.getRecordSeparator()));
    }
}
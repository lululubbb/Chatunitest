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
import java.lang.reflect.InvocationTargetException;
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
        StringBuilder out = new StringBuilder();
        Object[] values = {"value1", "value2", "value3"};
        csvFormat.printRecord(out, values);
        String result = out.toString();
        assertNotNull(result);
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws IOException {
        StringBuilder out = new StringBuilder();
        Object[] values = {};
        csvFormat.printRecord(out, values);
        String result = out.toString();
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValues() throws IOException {
        StringBuilder out = new StringBuilder();
        Object[] values = {null, "test", null};
        csvFormat.printRecord(out, values);
        String result = out.toString();
        assertNotNull(result);
        // nulls may be printed as nullString or empty, check that output contains "test"
        assertTrue(result.contains("test"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintAndPrintln() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Appendable mockOut = mock(Appendable.class);
        Object[] values = {"val1", "val2"};

        // Spy csvFormat to verify print calls
        CSVFormat spyFormat = spy(csvFormat);

        // Use reflection to get the print(Object, Appendable, boolean) method and stub it to call real method
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return printMethod.invoke(spyFormat, args[0], args[1], args[2]);
        }).when(spyFormat).print(any(), eq(mockOut), anyBoolean());

        // Use reflection to get the println(Appendable) method and stub it to call real method
        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return printlnMethod.invoke(spyFormat, args[0]);
        }).when(spyFormat).println(eq(mockOut));

        // Call printRecord on spy
        spyFormat.printRecord(mockOut, values);

        // Verify print called for each value with correct newRecord flag
        verify(spyFormat, times(values.length)).print(any(), eq(mockOut), anyBoolean());

        // Verify println called once
        verify(spyFormat, times(1)).println(eq(mockOut));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvoke() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder out = new StringBuilder();
        Object[] values = {"a", "b"};

        Method printRecordMethod = CSVFormat.class.getDeclaredMethod("printRecord", Appendable.class, Object[].class);
        printRecordMethod.setAccessible(true);

        // IMPORTANT: Wrap values in Object[] to match varargs parameter as a single argument
        printRecordMethod.invoke(csvFormat, out, (Object) values);

        String result = out.toString();
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }
}
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
        Appendable out = new StringBuilder();
        Object[] values = {"value1", "value2", "value3"};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertTrue(result.startsWith("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithEmptyValues() throws IOException {
        Appendable out = new StringBuilder();
        Object[] values = new Object[0];

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNullValue() throws IOException {
        Appendable out = new StringBuilder();
        Object[] values = {null};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        // null should be printed as empty or nullString if set
        String nullString = csvFormat.getNullString();
        if (nullString != null) {
            assertTrue(result.startsWith(nullString));
        } else {
            assertTrue(result.startsWith(""));
        }
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithSingleValue() throws IOException {
        Appendable out = new StringBuilder();
        Object[] values = {"singleValue"};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertEquals("singleValue" + csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordInvokesPrintProperly() throws Exception {
        Appendable out = new StringBuilder();
        Object[] values = {"a", "b"};

        // Spy on CSVFormat instance
        CSVFormat spyFormat = spy(csvFormat);

        // Use reflection to get the private print(Object, Appendable, boolean) method
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Stub printRecord to call real method
        doCallRealMethod().when(spyFormat).printRecord(any(Appendable.class), any());

        // Stub private print method to do nothing using doAnswer on spy and reflection
        doAnswer(invocation -> {
            // do nothing
            return null;
        }).when(spyFormat).print(any(), any(), anyBoolean());

        // Stub println to do nothing
        doNothing().when(spyFormat).println(any());

        spyFormat.printRecord(out, values);

        verify(spyFormat, times(2)).print(any(), eq(out), anyBoolean());
        verify(spyFormat, times(1)).println(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithEmptyStringValue() throws IOException {
        Appendable out = new StringBuilder();
        Object[] values = {""};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertEquals("" + csvFormat.getRecordSeparator(), result);
    }

}
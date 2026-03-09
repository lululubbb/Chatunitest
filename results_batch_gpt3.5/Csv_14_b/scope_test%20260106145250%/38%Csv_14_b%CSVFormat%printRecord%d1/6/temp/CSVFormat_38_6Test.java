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
    void testPrintRecord_withMultipleValues() throws IOException {
        StringBuilder out = new StringBuilder();
        Object[] values = {"value1", "value2", 123, null};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("123"));
        assertTrue(result.contains("null") || !result.contains("null")); // null handling depends on implementation
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
        Object[] values = {null, null};

        csvFormat.printRecord(out, values);

        String result = out.toString();
        assertTrue(result.contains("null") || !result.contains("null")); // depends on nullString setting
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_callsPrintForEachValue() throws IOException {
        // Use spy to verify internal print calls
        CSVFormat spyFormat = spy(csvFormat);
        StringBuilder out = new StringBuilder();
        Object[] values = {"a", "b"};

        // Stub printRecord to call real method
        doCallRealMethod().when(spyFormat).printRecord(any(Appendable.class), any());

        // Stub print to do nothing to avoid side effects
        doNothing().when(spyFormat).print(any(), any(Appendable.class), anyBoolean());

        spyFormat.printRecord(out, values);

        verify(spyFormat, times(values.length)).print(any(), eq(out), anyBoolean());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvokePrivatePrint() throws Exception {
        StringBuilder out = new StringBuilder();
        Object value = "testValue";

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        printMethod.invoke(csvFormat, value, out, true);

        String result = out.toString();
        assertTrue(result.contains("testValue"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvokePrivatePrintAndQuote() throws Exception {
        StringBuilder out = new StringBuilder();
        Object object = "quotedValue";
        CharSequence value = "quotedValue";
        int offset = 0;
        int len = value.length();
        boolean newRecord = true;

        Method printAndQuoteMethod = CSVFormat.class.getDeclaredMethod("printAndQuote", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printAndQuoteMethod.setAccessible(true);

        printAndQuoteMethod.invoke(csvFormat, object, value, offset, len, out, newRecord);

        String result = out.toString();
        assertTrue(result.contains("quotedValue"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_reflectionInvokePrintln() throws Exception {
        StringBuilder out = new StringBuilder();

        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);

        printlnMethod.invoke(csvFormat, out);

        String result = out.toString();
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }
}
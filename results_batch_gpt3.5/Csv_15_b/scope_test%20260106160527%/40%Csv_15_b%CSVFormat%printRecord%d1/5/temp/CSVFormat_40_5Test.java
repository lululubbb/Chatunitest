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
import org.mockito.invocation.InvocationOnMock;

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
        // Should contain all values separated by delimiter and a record separator at end
        assertTrue(result.startsWith("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
        assertTrue(result.endsWith(csvFormat.getRecordSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyValues() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out);
        String result = out.toString();
        // Should only print a record separator (empty record)
        assertEquals(csvFormat.getRecordSeparator(), result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValue() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, (Object) null);
        String result = out.toString();
        // Should print nullString or empty string followed by record separator
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
    void testPrintRecord_invokesPrivatePrintAndPrintln() throws Exception {
        Appendable mockOut = mock(Appendable.class);

        // Create a spy of CSVFormat.DEFAULT
        CSVFormat spyFormat = spy(CSVFormat.DEFAULT);

        // Use real method call for printRecord
        doCallRealMethod().when(spyFormat).printRecord(any(Appendable.class), any());

        // Use reflection to get private print and println methods
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);
        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);

        // Stub the private methods using reflection and doAnswer
        doAnswer(invocation -> {
            // call real private print method
            printMethod.invoke(spyFormat, invocation.getArgument(0), invocation.getArgument(1), invocation.getArgument(2));
            return null;
        }).when(spyFormat).print(any(), any(), anyBoolean());

        doAnswer(invocation -> {
            // call real private println method
            printlnMethod.invoke(spyFormat, invocation.getArgument(0));
            return null;
        }).when(spyFormat).println(any());

        spyFormat.printRecord(mockOut, "val1", "val2");

        // Verify private print called twice (for two values)
        verify(spyFormat, times(2)).print(any(), eq(mockOut), anyBoolean());
        // Verify println called once
        verify(spyFormat).println(mockOut);
    }
}
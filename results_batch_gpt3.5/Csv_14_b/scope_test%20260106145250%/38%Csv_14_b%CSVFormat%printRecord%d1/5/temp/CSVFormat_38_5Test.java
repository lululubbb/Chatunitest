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
import org.mockito.ArgumentMatchers;

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithMultipleValues() throws Exception {
        Appendable out = new StringBuilder();

        // Spy on csvFormat to verify print calls
        CSVFormat spyFormat = spy(csvFormat);

        // Use doCallRealMethod() when invoking printRecord to call actual method on spy
        doCallRealMethod().when(spyFormat).printRecord(ArgumentMatchers.any(Appendable.class), ArgumentMatchers.any());

        // Invoke printRecord on spyFormat
        spyFormat.printRecord(out, "value1", "value2", "value3");

        // Verify print called for each value, first with newRecord true, others false
        verify(spyFormat).print("value1", out, true);
        verify(spyFormat).print("value2", out, false);
        verify(spyFormat).print("value3", out, false);

        // Verify println called once
        verify(spyFormat).println(out);

        // Check the output contains all values (basic check)
        String result = out.toString();
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
        assertTrue(result.contains("value3"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNoValues() throws IOException {
        Appendable out = new StringBuilder();

        CSVFormat spyFormat = spy(csvFormat);

        doCallRealMethod().when(spyFormat).printRecord(ArgumentMatchers.any(Appendable.class), ArgumentMatchers.any());

        spyFormat.printRecord(out);

        verify(spyFormat, never()).print(any(), any(), anyBoolean());
        verify(spyFormat).println(out);

        assertEquals(System.lineSeparator(), out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNullValue() throws IOException {
        Appendable out = new StringBuilder();

        CSVFormat spyFormat = spy(csvFormat);

        doCallRealMethod().when(spyFormat).printRecord(ArgumentMatchers.any(Appendable.class), ArgumentMatchers.any());

        spyFormat.printRecord(out, (Object) null);

        verify(spyFormat).print(null, out, true);
        verify(spyFormat).println(out);

        String result = out.toString();
        assertNotNull(result);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordReflectionInvoke() throws Exception {
        Appendable out = new StringBuilder();

        // The printRecord method takes (Appendable, Object...) which is compiled as (Appendable, Object[])
        Method printRecordMethod = CSVFormat.class.getDeclaredMethod("printRecord", Appendable.class, Object[].class);
        printRecordMethod.setAccessible(true);

        // When invoking varargs reflectively, the varargs arguments must be wrapped in an Object[]
        printRecordMethod.invoke(csvFormat, out, new Object[] { "a", "b" });

        String output = out.toString();
        assertTrue(output.contains("a"));
        assertTrue(output.contains("b"));
    }
}
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

    private static final String CRLF = "\r\n";

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_noValues() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out);
        assertEquals(CRLF, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_singleValue() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "value1");
        String output = out.toString();
        assertTrue(output.endsWith(CRLF));
        assertTrue(output.contains("value1"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_multipleValues() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "val1", "val2", "val3");
        String output = out.toString();
        assertTrue(output.endsWith(CRLF));
        assertTrue(output.contains("val1"));
        assertTrue(output.contains("val2"));
        assertTrue(output.contains("val3"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValue() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, (Object) null);
        String output = out.toString();
        assertTrue(output.endsWith(CRLF));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_callsPrintMethod() throws Exception {
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        Appendable out = new StringBuilder();

        // Use reflection to get private print(Object, Appendable, boolean) method
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        spyFormat.printRecord(out, "a", "b");

        // Verify print(Object, Appendable, boolean) called with correct arguments
        verify(spyFormat, times(2)).print(any(), eq(out), anyBoolean());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_printThrowsIOException() throws Throwable {
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        Appendable out = new StringBuilder();

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        doThrow(new IOException("forced")).when(spyFormat).print(any(), eq(out), anyBoolean());

        IOException thrown = assertThrows(IOException.class, () -> spyFormat.printRecord(out, "val"));
        assertEquals("forced", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withEmptyStringValue() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "");
        String output = out.toString();
        assertTrue(output.endsWith(CRLF));
        // output.contains("") is always true, so no need to assert it
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMixedTypes() throws IOException {
        Appendable out = new StringBuilder();
        csvFormat.printRecord(out, "string", 123, true, 45.67, null);
        String output = out.toString();
        assertTrue(output.endsWith(CRLF));
        assertTrue(output.contains("string"));
        assertTrue(output.contains("123"));
        assertTrue(output.contains("true"));
        assertTrue(output.contains("45.67"));
    }
}
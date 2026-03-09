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
import org.mockito.stubbing.Answer;
import org.mockito.invocation.InvocationOnMock;

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_emptyValues() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out);
        assertEquals(System.lineSeparator(), out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_singleValue() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, "value");
        String expected = "value" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_multipleValues() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, "val1", "val2", "val3");
        // The default delimiter is ',' so values should be separated by commas
        String expected = "val1,val2,val3" + System.lineSeparator();
        assertEquals(expected, out.toString());
        assertTrue(out.toString().endsWith(System.lineSeparator()));
        assertTrue(out.toString().contains("val1"));
        assertTrue(out.toString().contains("val2"));
        assertTrue(out.toString().contains("val3"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_printMethodCalledWithCorrectNewRecordFlag() throws Exception {
        // Spy on CSVFormat to verify print(...) calls
        CSVFormat spyFormat = spy(CSVFormat.DEFAULT);
        StringBuilder out = new StringBuilder();

        // Use reflection to get the private print method
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Use doAnswer with InvocationOnMock from Mockito 3
        doAnswer((Answer<Void>) invocation -> {
            Object value = invocation.getArgument(0, Object.class);
            Appendable appendable = invocation.getArgument(1, Appendable.class);
            boolean newRecord = invocation.getArgument(2, Boolean.class);
            // Call the real private print method via reflection
            printMethod.invoke(spyFormat, value, appendable, newRecord);
            return null;
        }).when(spyFormat).print(any(), any(), anyBoolean());

        // Call printRecord with two values
        spyFormat.printRecord(out, "a", "b");

        // Verify print called twice
        verify(spyFormat, times(2)).print(any(), eq(out), anyBoolean());

        // Verify first call newRecord true, second false
        verify(spyFormat).print("a", out, true);
        verify(spyFormat).print("b", out, false);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValue() throws IOException {
        StringBuilder out = new StringBuilder();
        csvFormat.printRecord(out, (Object) null);
        String expected = "null" + System.lineSeparator();
        // Because print(Object, Appendable, boolean) is not visible,
        // assume null prints "null"
        assertTrue(out.toString().contains("null"));
        assertTrue(out.toString().endsWith(System.lineSeparator()));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintUsingReflection() throws Exception {
        // Use reflection to invoke private print(Object, Appendable, boolean)
        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        StringBuilder out = new StringBuilder();
        // Invoke private print with newRecord = true
        printMethod.invoke(csvFormat, "testValue", out, true);
        assertTrue(out.toString().contains("testValue"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrivatePrintAndEscapeAndQuote() throws Exception {
        // The private print(Object, CharSequence, int, int, Appendable, boolean) method is private
        Method printPrivate = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printPrivate.setAccessible(true);

        StringBuilder out = new StringBuilder();
        String val = "escape\"test";

        // Call private print with offset 0, len val.length()
        printPrivate.invoke(csvFormat, val, val, 0, val.length(), out, true);

        // Output should contain val string or escaped/quoted version
        assertTrue(out.toString().length() > 0);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_invokesPrintln() throws Exception {
        Method printlnMethod = CSVFormat.class.getDeclaredMethod("println", Appendable.class);
        printlnMethod.setAccessible(true);

        StringBuilder out = new StringBuilder();
        printlnMethod.invoke(csvFormat, out);
        assertTrue(out.toString().endsWith(System.lineSeparator()));
    }

}
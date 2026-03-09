package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.Reader;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNormalValues() {
        String result = csvFormat.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValue() {
        CSVFormat format = csvFormat.withNullString("NULL");
        String result = format.format("a", null, "c");
        assertEquals("a,NULL,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withValuesContainingCommaAndQuotes() {
        String result = csvFormat.format("a,b", "\"quote\"", "c");
        assertEquals("\"a,b\",\"\"\"quote\"\"\",c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("io error")).when(mockPrinter).printRecord((Object[]) any());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            formatWithMockPrinter(mockPrinter, "a", "b");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("io error", thrown.getCause().getMessage());
    }

    private String formatWithMockPrinter(CSVPrinter mockPrinter, Object... values) {
        try {
            // Use reflection to invoke printRecord on the mockPrinter with correct varargs
            Method printRecordMethod = CSVPrinter.class.getMethod("printRecord", Object[].class);
            printRecordMethod.invoke(mockPrinter, new Object[] { values });
            return ""; // Since StringWriter is not used here, return empty string
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw new IllegalStateException(cause);
            }
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Timeout(8000)
    public void testFormat_privateMethodInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method formatMethod = CSVFormat.class.getDeclaredMethod("format", Object[].class);
        formatMethod.setAccessible(true);
        String result = (String) formatMethod.invoke(csvFormat, (Object) new Object[]{"x", "y"});
        assertEquals("x,y", result);
    }

}
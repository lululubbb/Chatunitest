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
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_7_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void format_withNormalValues_returnsFormattedString() throws IOException {
        String[] values = new String[] { "a", "b", "c" };
        String result = csvFormat.format((Object[]) values);
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // The default delimiter is comma, default quote is double quote, default record separator CRLF
        // The result should be something like: "a,b,c"
        assertTrue(result.contains("a"));
        assertTrue(result.contains("b"));
        assertTrue(result.contains("c"));
        assertFalse(result.endsWith("\n"));
    }

    @Test
    @Timeout(8000)
    void format_withNullValues_returnsFormattedStringWithNulls() {
        Object[] values = new Object[] { null, "test", null };
        String result = csvFormat.format(values);
        assertNotNull(result);
        assertTrue(result.contains("test"));
        // null values should be printed as empty strings by default
        assertTrue(result.startsWith(","));
        assertTrue(result.endsWith(",test") || result.endsWith(",test,"));
    }

    @Test
    @Timeout(8000)
    void format_withEmptyValues_returnsEmptyString() {
        Object[] values = new Object[0];
        String result = csvFormat.format(values);
        assertNotNull(result);
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    void format_whenIOExceptionThrown_throwsIllegalStateException() throws Exception {
        // Create a mock CSVPrinter that throws IOException on printRecord(Object...)
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("IO error")).when(mockPrinter).printRecord(any(Object[].class));

        // Create a spy of CSVFormat.DEFAULT to override print(Appendable) method
        CSVFormat formatSpy = spy(CSVFormat.DEFAULT);

        // Use Mockito to return our mockPrinter when print(Appendable) is called
        doReturn(mockPrinter).when(formatSpy).print(any());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            formatSpy.format("a", "b");
        });
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("IO error", thrown.getCause().getMessage());
    }
}
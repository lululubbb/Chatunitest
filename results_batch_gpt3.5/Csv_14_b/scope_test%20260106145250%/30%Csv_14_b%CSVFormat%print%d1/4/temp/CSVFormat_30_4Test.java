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
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrintReturnsCSVPrinter() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = new StringBuilder();
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        // Access private final field 'out' in CSVPrinter to verify equals
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object out = outField.get(printer);
        assertSame(appendable, out);
    }

    @Test
    @Timeout(8000)
    void testPrintWithMockAppendable() throws IOException, NoSuchFieldException, IllegalAccessException {
        Appendable appendable = mock(Appendable.class);
        // Mock append(CharSequence) and append(CharSequence, int, int) to do nothing to avoid IOException during CSVPrinter construction
        when(appendable.append(any(CharSequence.class))).thenReturn(appendable);
        when(appendable.append(any(CharSequence.class), anyInt(), anyInt())).thenReturn(appendable);
        CSVPrinter printer = csvFormat.print(appendable);
        assertNotNull(printer);
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object out = outField.get(printer);
        assertSame(appendable, out);
    }

    @Test
    @Timeout(8000)
    void testPrintThrowsIOException() throws Exception {
        Appendable appendable = mock(Appendable.class);
        when(appendable.append(any(CharSequence.class))).thenThrow(new IOException("test exception"));
        when(appendable.append(any(CharSequence.class), anyInt(), anyInt())).thenThrow(new IOException("test exception"));

        IOException thrown = assertThrows(IOException.class, () -> {
            csvFormat.print(appendable);
        });

        assertEquals("test exception", thrown.getMessage());
    }
}
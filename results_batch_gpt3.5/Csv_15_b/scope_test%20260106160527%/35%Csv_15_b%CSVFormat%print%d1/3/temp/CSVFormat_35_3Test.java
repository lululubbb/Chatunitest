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

class CSVFormatPrintTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull_quoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString(null).withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder sb = new StringBuilder();
        format.print(null, sb, true);
        assertEquals("", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet_quoteModeAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.ALL);
        StringBuilder sb = new StringBuilder();
        format.print(null, sb, true);
        assertEquals("\"NULL\"", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringSet_quoteModeNotAll() throws IOException {
        CSVFormat format = csvFormat.withNullString("NULL").withQuoteMode(QuoteMode.MINIMAL);
        StringBuilder sb = new StringBuilder();
        format.print(null, sb, true);
        assertEquals("NULL", sb.toString());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueCharSequence_trimTrue() throws IOException {
        CSVFormat format = csvFormat.withTrim(true);
        StringBuilder sb = new StringBuilder();
        String value = "  test  ";
        format.print(value, sb, false);
        assertFalse(sb.toString().isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPrint_valueNotCharSequence_trimFalse() throws IOException {
        CSVFormat format = csvFormat.withTrim(false);
        StringBuilder sb = new StringBuilder();
        Integer value = 123;
        format.print(value, sb, false);
        assertTrue(sb.toString().contains("123"));
    }

    @Test
    @Timeout(8000)
    void testPrint_invokesPrivatePrintMethod() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        String value = "value";

        Method printMethod = CSVFormat.class.getDeclaredMethod("print", Object.class, CharSequence.class, int.class, int.class, Appendable.class, boolean.class);
        printMethod.setAccessible(true);

        // Call the private print method directly to check it works with normal input
        printMethod.invoke(csvFormat, value, value, 0, value.length(), sb, true);
        assertFalse(sb.toString().isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPrint_withMockAppendable_throwsIOException() throws IOException {
        Appendable out = mock(Appendable.class);
        doThrow(new IOException("mock IO exception")).when(out).append(any(CharSequence.class));
        IOException ex = assertThrows(IOException.class, () -> csvFormat.print("test", out, true));
        assertEquals("mock IO exception", ex.getMessage());
    }
}
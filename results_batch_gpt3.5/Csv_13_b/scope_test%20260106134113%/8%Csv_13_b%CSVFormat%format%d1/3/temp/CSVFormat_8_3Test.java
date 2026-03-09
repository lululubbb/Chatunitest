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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;
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
        // Default delimiter is comma, default quote is double quote, default recordSeparator is CRLF trimmed
        assertEquals("\"a\",\"b\",\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValues() {
        String result = csvFormat.format("a", null, "c");
        assertEquals("\"a\",,\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        String result = csvFormat.format("", "", "");
        assertEquals("\"\",\"\",\"\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNoValues() {
        String result = csvFormat.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        final StringWriter out = new StringWriter();

        // Create a mock CSVPrinter that throws IOException on printRecord(Object...)
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        // Cast any() to Object[] to resolve ambiguity of printRecord overloads
        doThrow(new IOException("mocked IO exception")).when(mockPrinter).printRecord((Object[]) any());

        // Spy on CSVFormat.DEFAULT to override print(Appendable) method to return mockPrinter
        CSVFormat spyFormat = spy(CSVFormat.DEFAULT);

        // Override print(Appendable) to return mockPrinter
        doReturn(mockPrinter).when(spyFormat).print(any());

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            // This will call format(), which calls print(out), which returns mockPrinter,
            // and then calls printRecord, throwing IOException to be wrapped in IllegalStateException
            spyFormat.format("a", "b");
        });
        assertTrue(ex.getCause() instanceof IOException);
        assertEquals("mocked IO exception", ex.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_char() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        method.setAccessible(true);
        // Test line breaks
        assertTrue((Boolean) method.invoke(null, '\r'));
        assertTrue((Boolean) method.invoke(null, '\n'));
        // Test non-line break
        assertFalse((Boolean) method.invoke(null, 'a'));
    }

    @Test
    @Timeout(8000)
    public void testPrivateIsLineBreak_Character() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("isLineBreak", Character.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\r')));
        assertTrue((Boolean) method.invoke(null, Character.valueOf('\n')));
        assertFalse((Boolean) method.invoke(null, Character.valueOf('a')));
        assertFalse((Boolean) method.invoke(null, (Character) null));
    }
}
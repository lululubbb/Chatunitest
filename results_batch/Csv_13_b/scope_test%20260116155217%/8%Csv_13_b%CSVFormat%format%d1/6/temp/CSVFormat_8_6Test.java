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
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_8_6Test {

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
        CSVFormat formatWithNullString = csvFormat.withNullString("NULL");
        String result = formatWithNullString.format("a", null, "c");
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
    public void testFormat_withQuoteCharacter() {
        CSVFormat formatWithQuote = csvFormat.withQuote('"');
        String result = formatWithQuote.format("a,b", "c\"d", "e");
        assertTrue(result.contains("\"a,b\""));
        assertTrue(result.contains("\"c\"\"d\""));
    }

    @Test
    @Timeout(8000)
    public void testFormat_throwsIllegalStateExceptionOnIOException() throws Exception {
        // Mock Appendable to throw IOException on append(CharSequence)
        Appendable mockAppendable = mock(Appendable.class);
        doThrow(new IOException("mock IO exception")).when(mockAppendable).append(any(CharSequence.class));

        // Use reflection to instantiate CSVPrinter with mockAppendable and csvFormat
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVPrinter printer = constructor.newInstance(mockAppendable, csvFormat);

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            printer.printRecord("a");
        });

        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("mock IO exception", thrown.getCause().getMessage());
    }
}
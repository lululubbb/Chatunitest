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
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CSVFormat_7_2Test {

    @Test
    @Timeout(8000)
    public void testFormat_withValues() {
        CSVFormat format = CSVFormat.DEFAULT;

        String result = format.format("a", "b", "c");
        assertEquals("a,b,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withNullValue() {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        String result = format.format("a", null, "c");
        assertEquals("a,NULL,c", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withEmptyValues() {
        CSVFormat format = CSVFormat.DEFAULT;

        String result = format.format();
        assertEquals("", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withQuoteModeAllNonNull() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL_NON_NULL);

        String result = format.format("a", "b", "c");
        assertEquals("\"a\",\"b\",\"c\"", result);
    }

    @Test
    @Timeout(8000)
    public void testFormat_withExceptionThrown() throws Exception {
        // Mock CSVPrinter to throw IOException on printRecord(Object...)
        CSVPrinter mockPrinter = mock(CSVPrinter.class);
        doThrow(new IOException("Forced IOException")).when(mockPrinter).printRecord((Object[]) any());

        CSVFormat format = CSVFormat.DEFAULT;

        // Spy the CSVFormat instance
        CSVFormat formatSpy = spy(format);

        // Override printer() via spy with no arguments
        doReturn(mockPrinter).when(formatSpy).printer();

        Executable executable = () -> formatSpy.format("a", "b");

        IllegalStateException thrown = assertThrows(IllegalStateException.class, executable);
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("Forced IOException", thrown.getCause().getMessage());
    }
}
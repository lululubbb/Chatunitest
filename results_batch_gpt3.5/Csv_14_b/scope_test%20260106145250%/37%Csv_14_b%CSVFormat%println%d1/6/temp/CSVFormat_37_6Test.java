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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVFormat_37_6Test {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        // Create a CSVFormat instance with default settings
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterAndRecordSeparator() throws IOException {
        // Create a CSVFormat with trailingDelimiter = true and recordSeparator = "\n"
        csvFormat = csvFormat.withTrailingDelimiter(true).withRecordSeparator("\n");

        csvFormat.println(appendable);

        InOrder inOrder = inOrder(appendable);
        inOrder.verify(appendable).append(csvFormat.getDelimiter());
        inOrder.verify(appendable).append("\n");
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterFalseAndRecordSeparator() throws IOException {
        // trailingDelimiter = false, recordSeparator = "\r\n"
        csvFormat = csvFormat.withTrailingDelimiter(false).withRecordSeparator("\r\n");

        csvFormat.println(appendable);

        verify(appendable, never()).append(csvFormat.getDelimiter());
        verify(appendable).append("\r\n");
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterTrueAndNullRecordSeparator() throws IOException {
        // trailingDelimiter = true, recordSeparator = null
        csvFormat = csvFormat.withTrailingDelimiter(true).withRecordSeparator(null);

        csvFormat.println(appendable);

        verify(appendable).append(csvFormat.getDelimiter());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterFalseAndNullRecordSeparator() throws IOException {
        // trailingDelimiter = false, recordSeparator = null
        csvFormat = csvFormat.withTrailingDelimiter(false).withRecordSeparator(null);

        csvFormat.println(appendable);

        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void testPrintln_AppendableThrowsIOException() throws IOException {
        csvFormat = csvFormat.withTrailingDelimiter(true).withRecordSeparator("\n");

        // Throw IOException on append(CharSequence)
        doThrow(new IOException("Append failed")).when(appendable).append(any(CharSequence.class));

        IOException thrown = assertThrows(IOException.class, () -> csvFormat.println(appendable));
        assertEquals("Append failed", thrown.getMessage());

        // Reset mock for second call to append(char)
        reset(appendable);
        doNothing().when(appendable).append(anyChar());
        doThrow(new IOException("Append failed")).when(appendable).append(any(CharSequence.class));

        // Test exception on second append call
        IOException thrown2 = assertThrows(IOException.class, () -> csvFormat.println(appendable));
        assertEquals("Append failed", thrown2.getMessage());
    }
}
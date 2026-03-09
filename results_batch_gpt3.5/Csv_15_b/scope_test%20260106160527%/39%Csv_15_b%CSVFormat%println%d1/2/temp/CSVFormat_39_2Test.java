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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_39_2Test {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        // Use the default CSVFormat instance for tests, can be customized in each test
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterTrueAndRecordSeparatorNotNull() throws IOException {
        // Create CSVFormat instance with trailingDelimiter true and recordSeparator non-null
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");
        Appendable out = mock(Appendable.class);

        format.println(out);

        // Verify delimiter appended first
        verify(out).append(eq(format.getDelimiter()));
        // Verify record separator appended second
        verify(out).append(format.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterTrueAndRecordSeparatorNull() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator(null);
        Appendable out = mock(Appendable.class);

        format.println(out);

        verify(out).append(eq(format.getDelimiter()));
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterFalseAndRecordSeparatorNotNull() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator("\r\n");
        Appendable out = mock(Appendable.class);

        format.println(out);

        verify(out, never()).append(anyChar());
        verify(out).append(format.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintln_WithTrailingDelimiterFalseAndRecordSeparatorNull() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator(null);
        Appendable out = mock(Appendable.class);

        format.println(out);

        // Appendable.append(char) and Appendable.append(CharSequence) are both possible,
        // so verify no append calls at all.
        verify(out, never()).append(anyChar());
        verify(out, never()).append(any(CharSequence.class));
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintln_ThrowsIOException() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");

        // Make append throw IOException on delimiter append
        doThrow(new IOException("append error")).when(out).append(eq(format.getDelimiter()));

        IOException thrown = assertThrows(IOException.class, () -> format.println(out));
        assertEquals("append error", thrown.getMessage());
    }
}
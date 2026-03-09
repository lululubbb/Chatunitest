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

class CSVFormatPrintlnTest {

    private CSVFormat csvFormat;

    @BeforeEach
    void setUp() {
        // Create a CSVFormat instance with default constructor parameters for testing
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterAndRecordSeparator_AppendsDelimiterAndRecordSeparator() throws IOException {
        // Use a CSVFormat with trailingDelimiter = true and a recordSeparator set
        CSVFormat format = csvFormat.withTrailingDelimiter(true).withRecordSeparator("\n");
        Appendable appendable = mock(Appendable.class);

        format.println(appendable);

        verify(appendable).append(format.getDelimiter());
        verify(appendable).append(format.getRecordSeparator());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterFalseAndRecordSeparator_AppendsOnlyRecordSeparator() throws IOException {
        // trailingDelimiter = false, recordSeparator != null
        CSVFormat format = csvFormat.withTrailingDelimiter(false).withRecordSeparator("\r\n");
        Appendable appendable = mock(Appendable.class);

        format.println(appendable);

        verify(appendable, never()).append(format.getDelimiter());
        verify(appendable).append(format.getRecordSeparator());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterTrueAndNullRecordSeparator_AppendsOnlyDelimiter() throws IOException {
        // trailingDelimiter = true, recordSeparator = null
        CSVFormat format = csvFormat.withTrailingDelimiter(true).withRecordSeparator(null);
        Appendable appendable = mock(Appendable.class);

        format.println(appendable);

        verify(appendable).append(format.getDelimiter());
        verify(appendable, never()).append((String) isNull());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterFalseAndNullRecordSeparator_AppendsNothing() throws IOException {
        // trailingDelimiter = false, recordSeparator = null
        CSVFormat format = csvFormat.withTrailingDelimiter(false).withRecordSeparator(null);
        Appendable appendable = mock(Appendable.class);

        format.println(appendable);

        verify(appendable, never()).append(anyChar());
        verify(appendable, never()).append(anyString());
        verifyNoMoreInteractions(appendable);
    }

    @Test
    @Timeout(8000)
    void println_ThrowsIOException_Propagates() throws IOException {
        Appendable appendable = mock(Appendable.class);
        doThrow(new IOException("append failed")).when(appendable).append(anyChar());

        CSVFormat format = csvFormat.withTrailingDelimiter(true).withRecordSeparator("\n");

        IOException thrown = assertThrows(IOException.class, () -> format.println(appendable));
        assertEquals("append failed", thrown.getMessage());
    }
}
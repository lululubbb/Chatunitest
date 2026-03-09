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
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVFormat_39_6Test {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterAndRecordSeparator_AppendsDelimiterAndRecordSeparator() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");
        csvFormat.println(appendable);
        verify(appendable).append(csvFormat.getDelimiter());
        verify(appendable).append("\n");
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterFalseAndRecordSeparator_AppendsRecordSeparatorOnly() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator("\r\n");
        csvFormat.println(appendable);
        verify(appendable, never()).append(csvFormat.getDelimiter());
        verify(appendable).append("\r\n");
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterTrueAndNullRecordSeparator_AppendsDelimiterOnly() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator(null);
        csvFormat.println(appendable);
        verify(appendable).append(csvFormat.getDelimiter());
        verify(appendable, never()).append(anyString());
    }

    @Test
    @Timeout(8000)
    void println_WithTrailingDelimiterFalseAndNullRecordSeparator_AppendsNothing() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator(null);
        csvFormat.println(appendable);
        verify(appendable, never()).append(anyChar());
        verify(appendable, never()).append(anyString());
    }

    @Test
    @Timeout(8000)
    void println_AppendableThrowsIOException_PropagatesException() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");
        // Fix: append(char) must be stubbed with the exact char value returned by getDelimiter()
        doThrow(new IOException("append failed")).when(appendable).append(csvFormat.getDelimiter());
        IOException thrown = assertThrows(IOException.class, () -> csvFormat.println(appendable));
        assertEquals("append failed", thrown.getMessage());
    }
}
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
import org.mockito.ArgumentMatchers;

class CSVFormatPrintlnTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterAndRecordSeparator_appendsDelimiterAndRecordSeparator() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");
        csvFormat.println(out);
        verify(out).append(Character.toString(csvFormat.getDelimiter()));
        verify(out).append("\n");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterFalseAndRecordSeparator_appendsOnlyRecordSeparator() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator("\r\n");
        csvFormat.println(out);
        verify(out, never()).append(ArgumentMatchers.anyChar());
        verify(out).append("\r\n");
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterTrueAndRecordSeparatorNull_appendsOnlyDelimiter() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator(null);
        csvFormat.println(out);
        verify(out).append(Character.toString(csvFormat.getDelimiter()));
        verify(out, never()).append((String) isNull());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterFalseAndRecordSeparatorNull_appendsNothing() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator(null);
        csvFormat.println(out);
        verify(out, never()).append(ArgumentMatchers.anyChar());
        verify(out, never()).append(ArgumentMatchers.anyString());
        verifyNoMoreInteractions(out);
    }
}
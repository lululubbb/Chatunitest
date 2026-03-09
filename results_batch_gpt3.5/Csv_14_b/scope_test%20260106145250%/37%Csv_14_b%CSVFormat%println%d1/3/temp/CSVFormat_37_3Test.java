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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVFormat_37_3Test {

    private Appendable out;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterAndRecordSeparator_appendsDelimiterAndRecordSeparator() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");
        format.println(out);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(format.getDelimiter());
        inOrder.verify(out).append(format.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterFalseAndRecordSeparator_appendsOnlyRecordSeparator() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator("\r\n");
        format.println(out);

        verify(out, times(1)).append(format.getRecordSeparator());
        verify(out, never()).append(anyChar());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterTrueAndNullRecordSeparator_appendsOnlyDelimiter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator(null);
        format.println(out);

        verify(out, times(1)).append(format.getDelimiter());
        verify(out, never()).append(anyString());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_withTrailingDelimiterFalseAndNullRecordSeparator_appendsNothing() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator(null);
        format.println(out);

        verify(out, never()).append(anyChar());
        verify(out, never()).append(anyString());
        verifyNoMoreInteractions(out);
    }
}
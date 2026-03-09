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
import org.mockito.InOrder;

class CSVFormatPrintlnTest {

    private CSVFormat csvFormat;
    private Appendable out;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterTrue_recordSeparatorNotNull() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("\n");
        csvFormat.println(out);
        InOrder inOrder = inOrder(out);
        inOrder.verify(out).append(csvFormat.getDelimiter());
        inOrder.verify(out).append(csvFormat.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterTrue_recordSeparatorNull() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator(null);
        csvFormat.println(out);
        verify(out).append(csvFormat.getDelimiter());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterFalse_recordSeparatorNotNull() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator("\r\n");
        csvFormat.println(out);
        verify(out).append(csvFormat.getRecordSeparator());
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterFalse_recordSeparatorNull() throws IOException {
        csvFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator(null);
        csvFormat.println(out);
        verify(out, never()).append(any());
        verifyNoMoreInteractions(out);
    }
}
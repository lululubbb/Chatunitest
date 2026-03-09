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

    private CSVFormat csvFormatTrailingDelimiterTrue;
    private CSVFormat csvFormatTrailingDelimiterFalse;
    private Appendable mockAppendable;

    @BeforeEach
    void setUp() {
        csvFormatTrailingDelimiterTrue = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        csvFormatTrailingDelimiterFalse = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        mockAppendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterTrue_withRecordSeparator_appendsDelimiterAndRecordSeparator() throws IOException {
        CSVFormat format = csvFormatTrailingDelimiterTrue;

        format.println(mockAppendable);

        InOrder inOrder = inOrder(mockAppendable);
        inOrder.verify(mockAppendable).append(Character.valueOf(format.getDelimiter()));
        inOrder.verify(mockAppendable).append(format.getRecordSeparator());
        verifyNoMoreInteractions(mockAppendable);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterTrue_withNullRecordSeparator_appendsOnlyDelimiter() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator((String) null);

        format.println(mockAppendable);

        verify(mockAppendable).append(Character.valueOf(format.getDelimiter()));
        verifyNoMoreInteractions(mockAppendable);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterFalse_withRecordSeparator_appendsOnlyRecordSeparator() throws IOException {
        CSVFormat format = csvFormatTrailingDelimiterFalse;

        format.println(mockAppendable);

        verify(mockAppendable).append(format.getRecordSeparator());
        verifyNoMoreInteractions(mockAppendable);
    }

    @Test
    @Timeout(8000)
    void println_trailingDelimiterFalse_withNullRecordSeparator_appendsNothing() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false).withRecordSeparator((String) null);

        format.println(mockAppendable);

        verify(mockAppendable, never()).append(any());
    }

    @Test
    @Timeout(8000)
    void println_appendsThrowsIOException_propagates() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true).withRecordSeparator("X");
        doThrow(new IOException("append failed")).when(mockAppendable).append(any(CharSequence.class));

        IOException thrown = assertThrows(IOException.class, () -> format.println(mockAppendable));
        assertEquals("append failed", thrown.getMessage());
    }
}
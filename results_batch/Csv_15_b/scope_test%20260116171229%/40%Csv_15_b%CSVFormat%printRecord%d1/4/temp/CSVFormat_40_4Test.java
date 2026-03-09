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

class CSVFormatPrintRecordTest {

    private CSVFormat csvFormat;
    private Appendable appendable;

    @BeforeEach
    void setUp() {
        csvFormat = CSVFormat.DEFAULT;
        appendable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNoValues() throws IOException {
        csvFormat.printRecord(appendable);
        verify(appendable, times(1)).append("\r\n");
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithSingleValue() throws IOException {
        csvFormat.printRecord(appendable, "value1");
        verify(appendable, times(1)).append("value1");
        verify(appendable, times(1)).append("\r\n");
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithMultipleValues() throws IOException {
        csvFormat.printRecord(appendable, "value1", "value2", "value3");
        verify(appendable, times(1)).append("value1");
        verify(appendable, times(2)).append(",");
        verify(appendable, times(1)).append("value2");
        verify(appendable, times(1)).append("value3");
        verify(appendable, times(1)).append("\r\n");
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithNullValue() throws IOException {
        csvFormat.printRecord(appendable, (Object) null);
        verify(appendable, times(1)).append("null");
        verify(appendable, times(1)).append("\r\n");
    }

    @Test
    @Timeout(8000)
    void testPrintRecordWithMixedValues() throws IOException {
        csvFormat.printRecord(appendable, "value1", null, 123, true);
        verify(appendable, times(1)).append("value1");
        verify(appendable, times(3)).append(",");
        verify(appendable, times(1)).append("null");
        verify(appendable, times(1)).append("123");
        verify(appendable, times(1)).append("true");
        verify(appendable, times(1)).append("\r\n");
    }

    @Test
    @Timeout(8000)
    void testPrintRecordThrowsIOExceptionFromPrint() throws IOException {
        Appendable throwingAppendable = new Appendable() {
            @Override
            public Appendable append(CharSequence csq) throws IOException {
                throw new IOException("append failed");
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) throws IOException {
                throw new IOException("append failed");
            }

            @Override
            public Appendable append(char c) throws IOException {
                throw new IOException("append failed");
            }
        };
        IOException thrown = assertThrows(IOException.class, () -> csvFormat.printRecord(throwingAppendable, "value"));
        assertEquals("append failed", thrown.getMessage());
    }
}
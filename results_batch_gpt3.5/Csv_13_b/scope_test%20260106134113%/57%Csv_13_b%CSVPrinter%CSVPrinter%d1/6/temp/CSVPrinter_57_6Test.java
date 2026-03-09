package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_6Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, format));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(out, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsAndHeader_printsCommentsAndHeader() throws IOException {
        // Return String[] for getHeaderComments() to match method signature
        when(format.getHeaderComments()).thenReturn(new String[]{"comment1", "comment2"});
        when(format.getHeader()).thenReturn(new String[]{"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        // Verify append called at least once (for comments and header)
        verify(out, atLeastOnce()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNoHeaderComments_andSkipHeaderRecordTrue_doesNotPrintHeader() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(new String[]{"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);

        // Since skip header is true, append should not be called
        verify(out, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNoHeaderCommentsAndNoHeader_doesNotPrintAnything() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        verify(out, never()).append(anyChar());
    }
}
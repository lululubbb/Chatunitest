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

    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, formatMock));
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> new CSVPrinter(outMock, null));
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsAndHeader_printsCommentsAndHeader() throws IOException {
        Iterable<String> headerComments = java.util.Arrays.asList("comment1", null, "comment2");

        String[] header = new String[] { "h1", "h2" };

        when(formatMock.getHeaderComments()).thenReturn(headerComments);
        when(formatMock.getHeader()).thenReturn(header);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // verify printComment called for non-null comments and printRecord called for header
        verify(outMock, atLeast(2)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullHeaderCommentsAndSkipHeaderRecord() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(new String[] { "h1" });
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);
        // header should be skipped, so no append calls expected
        verify(outMock, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullHeaderCommentsAndNullHeader() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        verify(outMock, never()).append(any(CharSequence.class));
    }
}
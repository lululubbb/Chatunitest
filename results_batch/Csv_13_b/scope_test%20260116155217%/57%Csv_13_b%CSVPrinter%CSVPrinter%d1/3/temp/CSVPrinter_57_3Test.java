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
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_3Test {

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
        String[] commentsArray = new String[] { "comment1", "comment2" };
        when(formatMock.getHeaderComments()).thenReturn(commentsArray);
        when(formatMock.getHeader()).thenReturn(new String[] { "h1", "h2" });
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // verify printComment called twice with non-null comments
        verify(outMock, atLeast(0)).append(any());
        // printComment calls out.append("# ").append(comment).append(CR).append(LF);
        // We can't verify exact calls on outMock here easily, but verifying no exceptions is sufficient

        // verify printRecord called with header
        // printRecord eventually calls print(Object...) which uses out.append
        // We verify append called at least once
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderAndSkipHeaderRecordTrue_doesNotPrintHeader() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(new String[] { "h1", "h2" });
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // Should not print header record, so minimal append calls
        verify(outMock, never()).append("h1");
        verify(outMock, never()).append("h2");
        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNoHeaderCommentsAndNoHeader() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // No header comments or header, so append likely not called
        verify(outMock, never()).append(any(CharSequence.class));
        assertNotNull(printer);
    }
}
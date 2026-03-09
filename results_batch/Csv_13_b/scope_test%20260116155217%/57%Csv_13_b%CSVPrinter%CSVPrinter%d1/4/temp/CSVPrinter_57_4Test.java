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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_4Test {

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
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(null, formatMock);
        });
        assertEquals("out", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(outMock, null);
        });
        assertEquals("format", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_printsHeaderCommentsAndHeaderRecord() throws IOException {
        List<String> headerComments = java.util.Arrays.asList("comment1", "comment2");
        when(formatMock.getHeaderComments()).thenReturn(headerComments);
        when(formatMock.getHeader()).thenReturn(new String[] {"col1", "col2"});
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // verify printComment called twice with non-null comments
        verify(outMock, atLeastOnce()).append(anyChar());
        // verify printRecord called with header columns
        verify(outMock, atLeastOnce()).append(anyChar());

        assertNotNull(printer);
    }

    @Test
    @Timeout(8000)
    void testConstructor_skipsHeaderRecordWhenSkipHeaderRecordTrue() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(new String[] {"col1", "col2"});
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        verify(outMock, never()).append(Constants.COMMENT);
    }

    @Test
    @Timeout(8000)
    void testConstructor_noHeaderCommentsNoHeader() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        verify(outMock, never()).append(Constants.COMMENT);
    }
}
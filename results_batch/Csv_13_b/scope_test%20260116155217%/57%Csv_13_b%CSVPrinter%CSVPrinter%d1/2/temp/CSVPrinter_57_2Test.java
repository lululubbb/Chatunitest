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

class CSVPrinter_57_2Test {

    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void constructor_nullOut_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new CSVPrinter(null, formatMock));
        assertEquals("out", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void constructor_nullFormat_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> new CSVPrinter(outMock, null));
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void constructor_withHeaderCommentsAndHeader_printsCommentsAndHeader() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(new String[] {"comment1", "comment2"});
        when(formatMock.getHeader()).thenReturn(new String[] {"h1", "h2"});
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        // Mock append(CharSequence) to return outMock for chained calls
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        // Also mock append(char) to return outMock
        when(outMock.append(anyChar())).thenReturn(outMock);

        // Mock printComment and printRecord methods by spying CSVPrinter
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void constructor_withNullHeaderCommentsAndSkipHeaderRecord_doesNotPrint() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(new String[] {"h1", "h2"});
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        when(outMock.append(anyChar())).thenReturn(outMock);

        new CSVPrinter(outMock, formatMock);

        verify(outMock, never()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void constructor_withNullHeaderAndNullHeaderComments_doesNotPrint() throws IOException {
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        when(outMock.append(anyChar())).thenReturn(outMock);

        new CSVPrinter(outMock, formatMock);

        verify(outMock, never()).append(any(CharSequence.class));
    }
}
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

class CSVPrinter_57_5Test {

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
        String[] headerComments = new String[] { "comment1", null, "comment2" };
        when(format.getHeaderComments()).thenReturn(headerComments);
        when(format.getHeader()).thenReturn(new String[] { "h1", "h2" });
        when(format.getSkipHeaderRecord()).thenReturn(false);

        // Stub append(CharSequence) to return 'out' to avoid NullPointerException.
        when(out.append(any(CharSequence.class))).thenReturn(out);
        // Also stub append(char) to return 'out' since printComment/printRecord may call it.
        when(out.append(anyChar())).thenReturn(out);

        CSVPrinter printer = new CSVPrinter(out, format);

        // verify append called at least three times (two non-null comments + header record)
        verify(out, atLeast(3)).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeader_skipHeaderRecordTrue_printsCommentsOnly() throws IOException {
        String[] headerComments = new String[] { "comment1" };
        when(format.getHeaderComments()).thenReturn(headerComments);
        when(format.getHeader()).thenReturn(new String[] { "h1", "h2" });
        when(format.getSkipHeaderRecord()).thenReturn(true);

        when(out.append(any(CharSequence.class))).thenReturn(out);
        when(out.append(anyChar())).thenReturn(out);

        CSVPrinter printer = new CSVPrinter(out, format);

        verify(out, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testConstructor_noHeaderCommentsNoHeader() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        assertNotNull(printer);
    }
}
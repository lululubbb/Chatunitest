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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_3Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullOut_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(null, format);
        });
        assertEquals("out", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullFormat_throwsException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(out, null);
        });
        assertEquals("format", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsAndHeader_printsCommentsAndHeader() throws IOException {
        // Fix: use cast to Iterable and suppress unchecked warning to avoid type mismatch
        @SuppressWarnings("unchecked")
        Iterable<String> headerComments = (Iterable<String>)(Object) Arrays.asList("comment1", null, "comment2");
        when(format.getHeaderComments()).thenReturn(headerComments);
        when(format.getHeader()).thenReturn(new String[] {"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        String result = out.toString();
        assertTrue(result.contains("comment1"));
        assertTrue(result.contains("comment2"));
        assertTrue(result.contains("h1"));
        assertTrue(result.contains("h2"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsNullAndSkipHeaderRecordTrue_printsOnlyComments() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(new String[] {"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);

        String result = out.toString();
        assertFalse(result.contains("h1"));
        assertFalse(result.contains("h2"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderNull_doesNotPrintHeader() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        String result = out.toString();
        assertEquals("", result);
    }
}
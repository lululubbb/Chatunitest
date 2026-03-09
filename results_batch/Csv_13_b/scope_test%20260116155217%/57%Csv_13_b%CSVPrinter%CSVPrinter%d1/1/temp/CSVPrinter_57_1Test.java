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
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_1Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = new StringBuilder();
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullOut_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(null, format);
        });
        assertEquals("out", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(out, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsAndHeader_printsCommentsAndHeader() throws IOException {
        List<String> headerComments = Arrays.asList("comment1", "comment2", null);
        // Fix: use thenAnswer to return Iterable<String> because getHeaderComments() returns String[]
        when(format.getHeaderComments()).thenAnswer(invocation -> headerComments);
        when(format.getHeader()).thenReturn(new String[]{"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(false);

        CSVPrinter printer = new CSVPrinter(out, format);

        String result = out.toString();
        // Comments and header record should be printed
        assertTrue(result.contains("comment1"));
        assertTrue(result.contains("comment2"));
        assertTrue(result.contains("h1"));
        assertTrue(result.contains("h2"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNoHeaderCommentsAndSkipHeaderRecord() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(new String[]{"h1", "h2"});
        when(format.getSkipHeaderRecord()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);

        String result = out.toString();
        // No comments printed
        assertFalse(result.contains("comment"));
        // Header should not be printed
        assertFalse(result.contains("h1"));
        assertFalse(result.contains("h2"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNoHeaderAndNoHeaderComments() throws IOException {
        when(format.getHeaderComments()).thenReturn(null);
        when(format.getHeader()).thenReturn(null);

        CSVPrinter printer = new CSVPrinter(out, format);

        String result = out.toString();
        assertEquals("", result);
    }
}
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
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_57_1Test {

    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderCommentsAndHeader_printsCommentsAndHeader() throws Exception {
        // Arrange
        List<String> headerComments = Arrays.asList("comment1", null, "comment2");
        when(formatMock.getHeaderComments()).thenReturn(headerComments.toArray(new String[0]));
        when(formatMock.getHeader()).thenReturn(new String[] { "h1", "h2" });
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);

        // Mock Appendable.append(CharSequence) to return outMock to avoid NullPointerException
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        when(outMock.append(any(CharSequence.class), anyInt(), anyInt())).thenReturn(outMock);
        when(outMock.append(anyChar())).thenReturn(outMock);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // Verify append called for comments and header printing
        verify(outMock, atLeast(1)).append(any(CharSequence.class));
        verify(outMock, atLeast(1)).append(anyChar());

        // Also verify internal fields set correctly
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        assertSame(outMock, outField.get(printer));

        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(formatMock, formatField.get(printer));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullOut_throwsException() {
        // Arrange
        Appendable nullOut = null;

        // Act & Assert
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(nullOut, formatMock);
        });
        assertTrue(thrown.getMessage().contains("out"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullFormat_throwsException() {
        // Arrange
        CSVFormat nullFormat = null;

        // Act & Assert
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            new CSVPrinter(outMock, nullFormat);
        });
        assertTrue(thrown.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_withHeaderSkipped_doesNotPrintHeader() throws Exception {
        // Arrange
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(new String[] { "h1", "h2" });
        when(formatMock.getSkipHeaderRecord()).thenReturn(true);

        // Mock Appendable.append to avoid NPE
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        when(outMock.append(anyChar())).thenReturn(outMock);

        // Act
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // Verify no calls to append (no printing header)
        verify(outMock, never()).append(any(CharSequence.class));
        verify(outMock, never()).append(anyChar());
    }

    @Test
    @Timeout(8000)
    void testPrintComment_invokesAppend() throws Exception {
        // Arrange
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        when(outMock.append(anyChar())).thenReturn(outMock);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // Act
        printer.printComment("comment");

        // Assert
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
        verify(outMock, atLeastOnce()).append(contains("comment"));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withObjects_invokesPrint() throws Exception {
        // Arrange
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        when(outMock.append(anyChar())).thenReturn(outMock);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        Object[] record = new Object[] { "a", "b" };

        // Act
        printer.printRecord(record);

        // Assert
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withIterable_invokesPrint() throws Exception {
        // Arrange
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        when(outMock.append(any(CharSequence.class))).thenReturn(outMock);
        when(outMock.append(anyChar())).thenReturn(outMock);

        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        Iterable<String> record = Arrays.asList("x", "y");

        // Act
        printer.printRecord(record);

        // Assert
        verify(outMock, atLeastOnce()).append(any(CharSequence.class));
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsOut() throws IOException {
        // Arrange
        when(formatMock.getHeaderComments()).thenReturn(null);
        when(formatMock.getHeader()).thenReturn(null);
        when(formatMock.getSkipHeaderRecord()).thenReturn(false);
        CSVPrinter printer = new CSVPrinter(outMock, formatMock);

        // Act
        Appendable out = printer.getOut();

        // Assert
        assertSame(outMock, out);
    }
}
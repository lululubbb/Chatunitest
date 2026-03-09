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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class CSVPrinter_66_6Test {

    private Appendable outMock;
    private CSVFormat formatMock;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        printer = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void println_AppendsRecordSeparatorWhenNotNull() throws IOException, Exception {
        String recordSeparator = "\r\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        printer.println();

        InOrder inOrder = inOrder(formatMock, outMock);
        inOrder.verify(formatMock).getRecordSeparator();
        inOrder.verify(outMock).append(recordSeparator);

        // Verify newRecord field is true after println
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        boolean newRecordValue = (boolean) newRecordField.get(printer);
        assertTrue(newRecordValue);
    }

    @Test
    @Timeout(8000)
    void println_DoesNotAppendWhenRecordSeparatorIsNull() throws IOException, Exception {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        printer.println();

        verify(formatMock).getRecordSeparator();
        verify(outMock, never()).append(any(CharSequence.class));

        // Verify newRecord field is true after println
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        boolean newRecordValue = (boolean) newRecordField.get(printer);
        assertTrue(newRecordValue);
    }

    @Test
    @Timeout(8000)
    void println_ThrowsIOExceptionWhenAppendThrows() throws IOException {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);
        doThrow(new IOException("append failed")).when(outMock).append(recordSeparator);

        IOException thrown = assertThrows(IOException.class, () -> printer.println());
        assertEquals("append failed", thrown.getMessage());
    }
}
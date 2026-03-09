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

class CSVPrinter_68_2Test {

    private Appendable out;
    private CSVFormat format;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
    }

    private CSVPrinter createPrinter() throws IOException {
        return new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues_callsPrintlnOnly() throws IOException {
        CSVPrinter printer = createPrinter();
        printer.printRecord();
        verify(out).append('\n');
        verifyNoMoreInteractions(out);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withSingleValue_callsPrintAndPrintln() throws IOException {
        CSVPrinter printer = createPrinter();
        CSVPrinter spyPrinter = spy(printer);

        spyPrinter.printRecord((Object) "value1");

        verify(spyPrinter).print("value1");
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues_callsPrintForEachAndPrintln() throws IOException {
        CSVPrinter printer = createPrinter();
        CSVPrinter spyPrinter = spy(printer);

        Object[] values = {"value1", 123, null};
        spyPrinter.printRecord(values);

        for (Object value : values) {
            verify(spyPrinter).print(value);
        }
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withIOExceptionFromPrint_propagatesException() throws IOException {
        CSVPrinter printer = createPrinter();
        CSVPrinter spyPrinter = spy(printer);
        doThrow(new IOException("test exception")).when(spyPrinter).print(any());

        IOException thrown = assertThrows(IOException.class, () -> spyPrinter.printRecord((Object) "value"));

        assertEquals("test exception", thrown.getMessage());
    }
}
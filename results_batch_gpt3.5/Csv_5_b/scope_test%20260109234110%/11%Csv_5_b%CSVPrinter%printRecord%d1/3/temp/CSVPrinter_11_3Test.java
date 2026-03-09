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

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVPrinter_11_3Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withMultipleValues() throws IOException, NoSuchFieldException, IllegalAccessException {
        Object[] values = {"value1", 123, null, 45.67};

        // Spy on printer to verify print(Object) calls
        CSVPrinter spyPrinter = spy(printer);

        // Replace 'out' field in spyPrinter with a mock to avoid side effects
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(spyPrinter, mock(Appendable.class));

        // Use doNothing() to avoid calling real print method implementation which may throw IOException
        doNothing().when(spyPrinter).print(any());

        // Use doNothing() for println as well
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        // Verify print called for each value
        for (Object value : values) {
            verify(spyPrinter).print(value);
        }

        // Verify println called once after printing all values
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNoValues() throws IOException, NoSuchFieldException, IllegalAccessException {
        Object[] values = new Object[0];

        CSVPrinter spyPrinter = spy(printer);

        // Replace 'out' field in spyPrinter with a mock to avoid side effects
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(spyPrinter, mock(Appendable.class));

        doNothing().when(spyPrinter).print(any());
        doNothing().when(spyPrinter).println();

        spyPrinter.printRecord(values);

        // print should never be called
        verify(spyPrinter, never()).print(any());

        // println should be called once
        verify(spyPrinter).println();
    }

    @Test
    @Timeout(8000)
    void testPrintRecord_withNullValuesArray() {
        assertThrows(NullPointerException.class, () -> printer.printRecord((Object[]) null));
    }
}
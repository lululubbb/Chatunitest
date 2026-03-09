package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;

class CSVPrinter_2_4Test {

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_shouldCloseOut() throws IOException, NoSuchFieldException, IllegalAccessException {
        Closeable closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);
        // Create CSVPrinter with a dummy Appendable (non-closeable)
        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), format);

        // Use reflection to set the private final 'out' field to closeableOut
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier using reflection hack
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, closeableOut);

        printer.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() throws IOException {
        Appendable nonCloseableOut = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(nonCloseableOut, format);

        assertDoesNotThrow(printer::close);
    }
}
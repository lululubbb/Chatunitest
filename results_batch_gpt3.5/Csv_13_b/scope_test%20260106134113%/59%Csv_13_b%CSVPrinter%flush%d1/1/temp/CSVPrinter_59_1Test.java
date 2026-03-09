package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterFlushTest {

    CSVFormat format;

    @BeforeEach
    void setUp() {
        format = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_shouldCallFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable flushableOut = mock(Flushable.class);
        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), format);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier via reflection to allow modification of final field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        outField.set(printer, flushableOut);

        printer.flush();

        verify(flushableOut, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_shouldNotThrow() throws IOException {
        Appendable nonFlushableOut = mock(Appendable.class);
        CSVPrinter printer = new CSVPrinter(nonFlushableOut, format);

        assertDoesNotThrow(printer::flush);
    }
}
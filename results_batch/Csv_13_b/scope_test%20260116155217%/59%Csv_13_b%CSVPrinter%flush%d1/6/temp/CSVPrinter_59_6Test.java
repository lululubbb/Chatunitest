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
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_59_6Test {

    private Appendable outMockFlushable;
    private Appendable outMockNonFlushable;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() {
        formatMock = mock(CSVFormat.class);
        outMockFlushable = mock(Appendable.class, withSettings().extraInterfaces(Flushable.class));
        outMockNonFlushable = mock(Appendable.class);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_invokesFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        CSVPrinter printer = new CSVPrinter(outMockNonFlushable, formatMock);

        // Use reflection to set the private final 'out' field to the flushable mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier using reflection
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, outMockFlushable);

        printer.flush();

        verify((Flushable) outMockFlushable, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_doesNotThrow() throws IOException {
        CSVPrinter printer = new CSVPrinter(outMockNonFlushable, formatMock);

        // Should not throw exception
        assertDoesNotThrow(() -> printer.flush());
    }
}
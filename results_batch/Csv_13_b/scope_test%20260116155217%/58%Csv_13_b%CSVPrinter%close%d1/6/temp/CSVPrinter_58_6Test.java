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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {

    private CSVPrinter csvPrinter;
    private Appendable mockAppendable;

    @BeforeEach
    void setUp() throws IOException {
        mockAppendable = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(mockAppendable, format);
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_callsClose() throws IOException {
        Closeable closeableOut = mock(Closeable.class);
        setPrivateField(csvPrinter, "out", closeableOut);

        csvPrinter.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_doesNotThrow() throws IOException {
        // out is Appendable but not Closeable
        Appendable nonCloseableOut = mock(Appendable.class);
        setPrivateField(csvPrinter, "out", nonCloseableOut);

        assertDoesNotThrow(() -> csvPrinter.close());
    }

    @Test
    @Timeout(8000)
    void close_whenCloseThrowsIOException_propagatesException() throws IOException {
        Closeable closeableOut = mock(Closeable.class);
        doThrow(new IOException("close failed")).when(closeableOut).close();
        setPrivateField(csvPrinter, "out", closeableOut);

        IOException thrown = assertThrows(IOException.class, () -> csvPrinter.close());
        assertEquals("close failed", thrown.getMessage());
    }

    private static void setPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = null;
            Class<?> clazz = target.getClass();
            while (clazz != null) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            if (field == null) {
                throw new NoSuchFieldException(fieldName);
            }
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
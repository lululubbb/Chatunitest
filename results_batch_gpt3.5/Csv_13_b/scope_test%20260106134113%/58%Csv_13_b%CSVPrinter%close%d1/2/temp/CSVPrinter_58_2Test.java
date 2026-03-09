package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doThrow;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CSVPrinter_58_2Test {

    private static void setFinalField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = CSVPrinter.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testClose_whenOutIsCloseable_shouldCloseOut() throws IOException, NoSuchFieldException, IllegalAccessException {
        Closeable closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), format);

        setFinalField(printer, "out", closeableOut);

        printer.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    public void testClose_whenOutIsCloseableAndCloseThrows_shouldThrowIOException() throws IOException, NoSuchFieldException, IllegalAccessException {
        Closeable closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), format);

        setFinalField(printer, "out", closeableOut);

        IOException ioException = new IOException("close failed");
        doThrow(ioException).when(closeableOut).close();

        IOException thrown = assertThrows(IOException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                printer.close();
            }
        });
        // optional: assert the exception message
        // assertEquals("close failed", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testClose_whenOutIsNotCloseable_shouldNotThrow() throws IOException {
        Appendable appendableOut = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(appendableOut, format);

        // Should not throw any exception
        printer.close();
    }
}
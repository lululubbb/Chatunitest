package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterFlushTest {

    private Appendable appendableMock;
    private CSVFormat csvFormatMock;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        csvFormatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendableMock, csvFormatMock);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_flushCalled() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable flushableMock = mock(Flushable.class);
        setPrivateField(csvPrinter, "out", flushableMock);

        csvPrinter.flush();

        verify(flushableMock, times(1)).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_noException() throws IOException {
        csvPrinter.flush();
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
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

        // Remove final modifier if present
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }
}
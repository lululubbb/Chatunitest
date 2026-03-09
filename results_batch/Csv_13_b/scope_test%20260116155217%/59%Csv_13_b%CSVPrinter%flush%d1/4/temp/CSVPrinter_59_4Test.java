package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.Flushable;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterFlushTest {

    private CSVPrinter csvPrinter;
    private Appendable appendableMock;
    private CSVFormat formatMock;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendableMock, formatMock);
    }

    private void setFinalField(Object target, String fieldName, Object newValue) throws Exception {
        Field field = CSVPrinter.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, newValue);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_shouldCallFlush() throws Exception {
        Flushable flushableMock = mock(Flushable.class);

        setFinalField(csvPrinter, "out", flushableMock);

        assertDoesNotThrow(() -> csvPrinter.flush());

        verify(flushableMock).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_shouldNotThrow() {
        // out is appendableMock which is not Flushable
        assertDoesNotThrow(() -> csvPrinter.flush());
    }

    @Test
    @Timeout(8000)
    void flush_whenFlushThrowsIOException_shouldThrow() throws Exception {
        Flushable flushableMock = mock(Flushable.class);
        doThrow(new IOException("flush error")).when(flushableMock).flush();

        setFinalField(csvPrinter, "out", flushableMock);

        IOException thrown = null;
        try {
            csvPrinter.flush();
        } catch (IOException e) {
            thrown = e;
        }
        assertNotNull(thrown);
        assertEquals("flush error", thrown.getMessage());
    }
}
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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {

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
    void close_whenOutIsCloseable_shouldCallClose() throws IOException, NoSuchFieldException, IllegalAccessException {
        Closeable closeableMock = mock(Closeable.class);
        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), csvFormatMock);

        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier using reflection on the "modifiers" field of Field class
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        outField.set(printer, closeableMock);

        printer.close();

        verify(closeableMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() {
        assertDoesNotThrow(() -> csvPrinter.close());
    }
}
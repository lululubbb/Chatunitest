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
    private CSVFormat formatMock;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendableMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_callsClose() throws IOException, NoSuchFieldException, IllegalAccessException {
        Closeable closeableMock = mock(Closeable.class);
        CSVPrinter printer = new CSVPrinter(appendableMock, formatMock);

        // Use reflection to set private final 'out' field to closeableMock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Set the field directly (final modifier removal not needed in Java 12+)
        outField.set(printer, closeableMock);

        printer.close();

        verify(closeableMock, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_doesNotThrow() throws IOException {
        // appendableMock is not Closeable, so close() should do nothing and not throw
        csvPrinter.close();
        // no exception expected, so test passes if no exception is thrown
    }
}
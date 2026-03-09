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

class CSVPrinter_58_5Test {

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
        Closeable closeableOut = mock(Closeable.class);
        CSVPrinter printerWithCloseable = new CSVPrinter(new Appendable() {
            @Override
            public Appendable append(CharSequence csq) {
                return this;
            }

            @Override
            public Appendable append(CharSequence csq, int start, int end) {
                return this;
            }

            @Override
            public Appendable append(char c) {
                return this;
            }
        }, csvFormatMock);

        // Use reflection to set the private final 'out' field to our Closeable mock
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printerWithCloseable, closeableOut);

        printerWithCloseable.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() throws IOException {
        // appendableMock is not Closeable, ensure no exception thrown
        assertDoesNotThrow(() -> csvPrinter.close());
    }
}
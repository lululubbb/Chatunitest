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
import java.lang.reflect.Constructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinterCloseTest {

    private Closeable mockCloseable;
    private CSVPrinter printerWithCloseable;
    private CSVPrinter printerWithNonCloseable;

    @BeforeEach
    void setUp() throws Exception {
        // Mock Closeable Appendable
        mockCloseable = mock(Closeable.class, withSettings().extraInterfaces(Appendable.class));
        CSVFormat format = mock(CSVFormat.class);

        // Create CSVPrinter with Closeable Appendable using reflection to access constructor
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        printerWithCloseable = constructor.newInstance((Appendable) mockCloseable, format);

        // Create CSVPrinter with non-Closeable Appendable
        Appendable nonCloseableAppendable = mock(Appendable.class);
        printerWithNonCloseable = constructor.newInstance(nonCloseableAppendable, format);
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_callsClose() throws IOException {
        printerWithCloseable.close();
        verify(mockCloseable, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_doesNotThrow() {
        assertDoesNotThrow(() -> printerWithNonCloseable.close());
    }
}
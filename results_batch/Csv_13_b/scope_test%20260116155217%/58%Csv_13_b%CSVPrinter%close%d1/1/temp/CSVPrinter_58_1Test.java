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

class CSVPrinter_58_1Test {

    private CSVPrinter csvPrinter;
    private Appendable mockAppendable;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() throws IOException {
        mockAppendable = mock(Appendable.class);
        mockFormat = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(mockAppendable, mockFormat);
    }

    @Test
    @Timeout(8000)
    void testClose_outIsCloseable_closesOut() throws Exception {
        Closeable mockCloseable = mock(Closeable.class);
        setOutField(csvPrinter, mockCloseable);

        csvPrinter.close();

        verify(mockCloseable, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void testClose_outIsNotCloseable_noException() throws Exception {
        // out is the mock Appendable which does NOT implement Closeable
        // Just invoking close() should not throw any exception
        assertDoesNotThrow(() -> csvPrinter.close());
    }

    private void setOutField(CSVPrinter printer, Object outInstance) throws Exception {
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        outField.set(printer, outInstance);
    }
}
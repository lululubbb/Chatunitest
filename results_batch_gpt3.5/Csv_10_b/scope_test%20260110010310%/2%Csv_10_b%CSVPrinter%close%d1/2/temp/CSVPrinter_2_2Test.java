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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class CSVPrinter_2_2Test {

    @Test
    @Timeout(8000)
    void close_WithCloseableOut_ShouldCallClose() throws IOException {
        Closeable closeableOut = mock(Closeable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), format);

        // Use reflection to set the private final field 'out' to closeableOut
        try {
            Field outField = CSVPrinter.class.getDeclaredField("out");
            outField.setAccessible(true);
            outField.set(printer, closeableOut);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed to set 'out' field: " + e.getMessage());
        }

        printer.close();

        verify(closeableOut, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_WithNonCloseableOut_ShouldNotThrow() throws IOException {
        Appendable nonCloseableOut = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(nonCloseableOut, format);

        assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                printer.close();
            }
        });
    }
}
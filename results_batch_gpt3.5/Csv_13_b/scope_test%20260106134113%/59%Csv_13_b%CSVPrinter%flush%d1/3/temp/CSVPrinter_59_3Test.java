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

    private Appendable appendable;
    private CSVFormat csvFormat;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws IOException {
        appendable = mock(Appendable.class);
        csvFormat = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendable, csvFormat);
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsFlushable_shouldCallFlush() throws IOException, NoSuchFieldException, IllegalAccessException {
        Flushable flushable = mock(Flushable.class);
        // Create CSVPrinter with a dummy Appendable (mock), then replace 'out' with flushable via reflection
        CSVPrinter printer = new CSVPrinter(mock(Appendable.class), csvFormat);

        // Use reflection to set private final field 'out' to flushable
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier for the 'out' field (works on Java 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~Modifier.FINAL);

        outField.set(printer, flushable);

        printer.flush();

        verify(flushable).flush();
    }

    @Test
    @Timeout(8000)
    void flush_whenOutIsNotFlushable_shouldNotThrow() throws IOException {
        // appendable is not Flushable
        csvPrinter.flush();
        // no exception means pass
    }
}
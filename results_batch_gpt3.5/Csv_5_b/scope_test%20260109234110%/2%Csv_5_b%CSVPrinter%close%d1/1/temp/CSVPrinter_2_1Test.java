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

class CSVPrinter_2_1Test {

    private Appendable appendable;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        appendable = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(appendable, format);
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsCloseable_shouldCloseOut() throws Exception {
        Closeable closeable = mock(Closeable.class);

        CSVPrinter printerWithCloseable = createCSVPrinterWithOut(closeable, format);

        printerWithCloseable.close();

        verify(closeable, times(1)).close();
    }

    @Test
    @Timeout(8000)
    void close_whenOutIsNotCloseable_shouldNotThrow() {
        assertDoesNotThrow(() -> printer.close());
    }

    private static CSVPrinter createCSVPrinterWithOut(Object out, CSVFormat format) throws Exception {
        // Use the public constructor to create CSVPrinter instance with a dummy Appendable
        Appendable dummyAppendable = new Appendable() {
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
        };

        CSVPrinter printer = new CSVPrinter(dummyAppendable, format);

        // Use reflection to set the private final field 'out' to the passed 'out' object
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);

        // Remove final modifier via reflection if necessary
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(outField, outField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

        outField.set(printer, out);

        return printer;
    }
}
package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_61_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
    }

    private CSVPrinter createPrinterWithFormat(Appendable out, CSVFormat format) throws Exception {
        CSVPrinter printer = new CSVPrinter(out, format);
        // Use reflection to set the private final field 'format' to the mock
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(printer, format);
        return printer;
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNull() throws Exception {
        when(format.getNullString()).thenReturn(null);

        printer = createPrinterWithFormat(out, format);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print((Object) null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringNotNull() throws Exception {
        when(format.getNullString()).thenReturn("NULL");

        printer = createPrinterWithFormat(out, format);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print((Object) null);

        verify(format).getNullString();
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws Exception {
        when(format.getNullString()).thenReturn(null);

        printer = createPrinterWithFormat(out, format);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        String testValue = "value123";

        spyPrinter.print(testValue);

        verify(format, never()).getNullString();
    }
}
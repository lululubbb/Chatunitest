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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CSVPrinter_61_6Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = new CSVPrinter(out, format);

        // Inject mocked format into printer using reflection to avoid constructor issues
        Field formatField = CSVPrinter.class.getDeclaredField("format");
        formatField.setAccessible(true);
        formatField.set(printer, format);
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringIsNull() throws IOException {
        when(format.getNullString()).thenReturn(null);

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.print(null);

        verify(format).getNullString();

        verify(out, never()).append((CharSequence) isNull());
    }

    @Test
    @Timeout(8000)
    void testPrint_nullValue_nullStringIsNonNull() throws IOException {
        when(format.getNullString()).thenReturn("NULL");

        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.print(null);

        verify(format).getNullString();

        verify(out, atLeastOnce()).append("NULL");
    }

    @Test
    @Timeout(8000)
    void testPrint_nonNullValue() throws IOException {
        String value = "testValue";
        CSVPrinter spyPrinter = Mockito.spy(printer);
        spyPrinter.print(value);

        verify(out, atLeastOnce()).append(value);
    }

}
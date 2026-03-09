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
import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVPrinter_68_1Test {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    public void setUp() throws Exception {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        printer = createCSVPrinter(out, format);
    }

    private CSVPrinter createCSVPrinter(Appendable out, CSVFormat format) throws Exception {
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        return constructor.newInstance(out, format);
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withMultipleValues() throws IOException {
        Object val1 = "value1";
        Object val2 = 123;
        Object val3 = null;

        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord(val1, val2, val3);

        verify(spyPrinter, times(1)).print(val1);
        verify(spyPrinter, times(1)).print(val2);
        verify(spyPrinter, times(1)).print(val3);
        verify(spyPrinter, times(1)).println();
    }

    @Test
    @Timeout(8000)
    public void testPrintRecord_withNoValues() throws IOException {
        CSVPrinter spyPrinter = Mockito.spy(printer);

        spyPrinter.printRecord();

        verify(spyPrinter, never()).print(any());
        verify(spyPrinter, times(1)).println();
    }
}
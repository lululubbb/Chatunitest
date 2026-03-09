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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_66_3Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        printer = new CSVPrinter(out, format);
    }

    @Test
    @Timeout(8000)
    void println_AppendsRecordSeparator_WhenRecordSeparatorIsNotNull() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        printer.println();

        verify(out).append(recordSeparator);
        assertTrue(getNewRecordFlag(printer));
    }

    @Test
    @Timeout(8000)
    void println_DoesNotAppend_WhenRecordSeparatorIsNull() throws IOException {
        when(format.getRecordSeparator()).thenReturn(null);

        printer.println();

        verify(out, never()).append(anyString());
        assertTrue(getNewRecordFlag(printer));
    }

    private boolean getNewRecordFlag(CSVPrinter printer) {
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            return field.getBoolean(printer);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
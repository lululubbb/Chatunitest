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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CSVPrinter_9_4Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private org.mockito.MockitoSession mocks;

    @BeforeEach
    void setUp() {
        mocks = org.mockito.Mockito.mockitoSession()
                .initMocks(this)
                .startMocking();
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.finishMocking();
        }
    }

    @Test
    @Timeout(8000)
    void println_RecordSeparatorNotNull_AppendsRecordSeparatorAndSetsNewRecordTrue() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        printer.println();

        verify(out).append(recordSeparator);
        // Access private field newRecord via reflection to check its value
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            boolean newRecordValue = field.getBoolean(printer);
            assertTrue(newRecordValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    void println_RecordSeparatorNull_DoesNotAppendButSetsNewRecordTrue() throws IOException {
        when(format.getRecordSeparator()).thenReturn(null);

        printer.println();

        verify(out, never()).append(anyString());
        // Access private field newRecord via reflection to check its value
        try {
            Field field = CSVPrinter.class.getDeclaredField("newRecord");
            field.setAccessible(true);
            boolean newRecordValue = field.getBoolean(printer);
            assertTrue(newRecordValue);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}
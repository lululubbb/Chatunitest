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
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoSession;
import org.mockito.quality.Strictness;

class CSVPrinter_9_5Test {

    @Mock
    private Appendable out;

    @Mock
    private CSVFormat format;

    private CSVPrinter printer;

    private MockitoSession mockitoSession;

    @BeforeEach
    void setUp() throws Exception {
        mockitoSession = org.mockito.Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.LENIENT)
                .startMocking();
        printer = new CSVPrinter(out, format);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mockitoSession != null) {
            mockitoSession.finishMocking();
        }
    }

    @Test
    @Timeout(8000)
    void testPrintln_appendsRecordSeparatorAndSetsNewRecordTrue() throws IOException, NoSuchFieldException, IllegalAccessException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        // set newRecord initially false to verify it is set to true after println
        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        newRecordField.set(printer, false);

        printer.println();

        verify(out).append(recordSeparator);
        assertTrue(newRecordField.getBoolean(printer));
    }

    @Test
    @Timeout(8000)
    void testPrintln_appendsCustomRecordSeparator() throws IOException {
        String recordSeparator = "\r\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);

        assertDoesNotThrow(() -> printer.println());

        verify(out).append(recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testPrintln_throwsIOException() throws IOException {
        String recordSeparator = "\n";
        when(format.getRecordSeparator()).thenReturn(recordSeparator);
        doThrow(new IOException("append failed")).when(out).append(recordSeparator);

        IOException exception = assertThrows(IOException.class, () -> printer.println());
        assertEquals("append failed", exception.getMessage());
    }
}
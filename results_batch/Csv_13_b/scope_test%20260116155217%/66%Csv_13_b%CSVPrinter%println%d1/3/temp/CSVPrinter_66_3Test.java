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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVPrinter_66_3Test {

    private CSVPrinter csvPrinter;
    private Appendable outMock;
    private CSVFormat formatMock;

    @BeforeEach
    public void setUp() throws IOException {
        outMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(outMock, formatMock);
    }

    @Test
    @Timeout(8000)
    public void testPrintln_withRecordSeparator() throws Exception {
        String recordSeparator = "\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);

        csvPrinter.println();

        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        boolean newRecordValue = (boolean) newRecordField.get(csvPrinter);

        assertTrue(newRecordValue, "newRecord should be true after println");
    }

    @Test
    @Timeout(8000)
    public void testPrintln_withNullRecordSeparator() throws Exception {
        when(formatMock.getRecordSeparator()).thenReturn(null);

        csvPrinter.println();

        Field newRecordField = CSVPrinter.class.getDeclaredField("newRecord");
        newRecordField.setAccessible(true);
        boolean newRecordValue = (boolean) newRecordField.get(csvPrinter);

        assertTrue(newRecordValue, "newRecord should be true after println");
    }

    @Test
    @Timeout(8000)
    public void testPrintln_appendThrowsIOException() throws Exception {
        String recordSeparator = "\r\n";
        when(formatMock.getRecordSeparator()).thenReturn(recordSeparator);
        when(outMock.append(recordSeparator)).thenThrow(new IOException("append failed"));

        IOException thrown = null;
        try {
            csvPrinter.println();
        } catch (IOException e) {
            thrown = e;
        }
        assertTrue(thrown != null && thrown.getMessage().contains("append failed"));
    }
}
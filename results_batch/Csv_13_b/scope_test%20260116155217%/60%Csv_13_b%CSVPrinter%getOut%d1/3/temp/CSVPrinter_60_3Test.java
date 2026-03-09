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

class CSVPrinter_60_3Test {

    private Appendable mockAppendable;
    private CSVFormat mockFormat;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws IOException {
        mockAppendable = mock(Appendable.class);
        mockFormat = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(mockAppendable, mockFormat);
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsSameAppendableInstance() throws Exception {
        // Use reflection to access private final field 'out'
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outValue = (Appendable) outField.get(csvPrinter);

        Appendable returnedOut = csvPrinter.getOut();

        assertSame(outValue, returnedOut, "getOut should return the internal Appendable instance");
    }
}
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

class CSVPrinter_60_1Test {

    private CSVPrinter csvPrinter;
    private Appendable mockAppendable;

    @BeforeEach
    void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        mockAppendable = mock(Appendable.class);

        // Create a real CSVFormat instance instead of mocking it, because CSVPrinter likely uses it internally
        CSVFormat format = CSVFormat.DEFAULT;

        csvPrinter = new CSVPrinter(mockAppendable, format);
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsSameAppendableInstance() throws Exception {
        // Using reflection to access private final field 'out'
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outValue = (Appendable) outField.get(csvPrinter);

        Appendable returned = csvPrinter.getOut();

        assertNotNull(returned);
        assertSame(outValue, returned);
    }
}
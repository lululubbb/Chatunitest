package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

class CSVPrinter_15_1Test {

    @Test
    @Timeout(8000)
    void testGetOut_returnsSameAppendableInstance() throws Exception {
        Appendable mockAppendable = Mockito.mock(Appendable.class);
        CSVFormat mockFormat = Mockito.mock(CSVFormat.class);

        // Create instance with mockAppendable and mockFormat
        CSVPrinter printer = new CSVPrinter(mockAppendable, mockFormat);

        // Direct call to getOut should return the same Appendable instance
        Appendable out = printer.getOut();
        assertSame(mockAppendable, out);

        // Use reflection to access private final field 'out' and verify it matches getOut()
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outFieldValue = (Appendable) outField.get(printer);
        assertSame(outFieldValue, out);
    }
}
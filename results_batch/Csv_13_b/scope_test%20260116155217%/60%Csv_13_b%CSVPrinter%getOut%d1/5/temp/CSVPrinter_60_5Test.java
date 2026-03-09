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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

public class CSVPrinter_60_5Test {

    private Appendable appendableMock;
    private CSVFormat formatMock;
    private CSVPrinter csvPrinter;

    @BeforeEach
    public void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        appendableMock = Mockito.mock(Appendable.class);
        formatMock = Mockito.mock(CSVFormat.class);

        // Since CSVPrinter's constructor is not fully shown, we must call the actual constructor
        // that initializes the final fields 'out' and 'format'.
        // If the constructor requires more parameters or different ones, adjust accordingly.
        csvPrinter = new CSVPrinter(appendableMock, formatMock);

        // Use reflection to set the private final fields if needed, but here the constructor should suffice.
    }

    @Test
    @Timeout(8000)
    public void testGetOutReturnsCorrectAppendable() throws IOException {
        Appendable out = csvPrinter.getOut();
        assertSame(appendableMock, out);
    }

    @Test
    @Timeout(8000)
    public void testGetOutReflectiveAccess() throws Exception {
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outFieldValue = (Appendable) outField.get(csvPrinter);

        Appendable methodReturn = csvPrinter.getOut();

        assertSame(outFieldValue, methodReturn);
    }
}
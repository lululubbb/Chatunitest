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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;

class CSVPrinter_60_6Test {

    private Appendable appendableMock;
    private CSVFormat csvFormatMock;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws IOException {
        appendableMock = Mockito.mock(Appendable.class);
        csvFormatMock = Mockito.mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(appendableMock, csvFormatMock);
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsOutField() throws Exception {
        // Use reflection to get private final field 'out'
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Object outFieldValue = outField.get(csvPrinter);

        Appendable result = csvPrinter.getOut();

        assertSame(outFieldValue, result, "getOut should return the private field 'out'");
    }

    @Test
    @Timeout(8000)
    void testGetOut_reflectionInvoke() throws Exception {
        Method getOutMethod = CSVPrinter.class.getDeclaredMethod("getOut");
        getOutMethod.setAccessible(true);

        Object result = getOutMethod.invoke(csvPrinter);

        assertSame(appendableMock, result);
    }
}
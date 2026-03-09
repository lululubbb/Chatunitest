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

class CSVPrinter_60_1Test {

    private Appendable mockAppendable;
    private CSVFormat mockFormat;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws Exception {
        mockAppendable = mock(Appendable.class);
        mockFormat = mock(CSVFormat.class);
        // Use reflection to invoke the constructor to avoid compilation errors if constructor is package-private
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        csvPrinter = constructor.newInstance(mockAppendable, mockFormat);
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsSameAppendableInstance() {
        Appendable out = csvPrinter.getOut();
        assertNotNull(out, "getOut should not return null");
        assertSame(mockAppendable, out, "getOut should return the appendable passed in constructor");
    }
}
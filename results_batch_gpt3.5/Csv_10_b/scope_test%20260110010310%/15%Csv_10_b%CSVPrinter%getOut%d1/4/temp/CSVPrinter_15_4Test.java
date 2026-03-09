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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;

class CSVPrinter_15_4Test {

    private Appendable mockAppendable;
    private CSVFormat mockFormat;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws Exception {
        mockAppendable = mock(Appendable.class);
        mockFormat = mock(CSVFormat.class);

        // Use reflection to get the constructor and create instance
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        printer = constructor.newInstance(mockAppendable, mockFormat);
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsAppendableInstance() {
        Appendable out = printer.getOut();
        assertNotNull(out);
        assertSame(mockAppendable, out);
    }
}
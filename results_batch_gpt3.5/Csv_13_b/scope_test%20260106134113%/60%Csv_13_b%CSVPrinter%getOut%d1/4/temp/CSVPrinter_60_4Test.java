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

class CSVPrinter_60_4Test {

    private Appendable appendableMock;
    private CSVFormat formatMock;
    private CSVPrinter csvPrinter;

    @BeforeEach
    void setUp() throws Exception {
        appendableMock = mock(Appendable.class);
        formatMock = mock(CSVFormat.class);

        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        csvPrinter = constructor.newInstance(appendableMock, formatMock);
    }

    @Test
    @Timeout(8000)
    void testGetOut_ReturnsAppendable() throws IOException {
        Appendable out = csvPrinter.getOut();
        assertNotNull(out);
        assertSame(appendableMock, out);
    }
}
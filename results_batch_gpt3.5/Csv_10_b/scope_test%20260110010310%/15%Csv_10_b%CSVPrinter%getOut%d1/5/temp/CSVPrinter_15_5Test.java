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

import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.lang.reflect.Constructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVPrinter_15_5Test {

    private Appendable appendableMock;
    private CSVFormat formatMock;
    private CSVPrinter csvPrinter;

    @BeforeEach
    public void setUp() throws Exception {
        appendableMock = Mockito.mock(Appendable.class);
        formatMock = Mockito.mock(CSVFormat.class);

        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        csvPrinter = constructor.newInstance(appendableMock, formatMock);
    }

    @Test
    @Timeout(8000)
    public void testGetOut_returnsTheSameAppendable() {
        Appendable returned = csvPrinter.getOut();
        assertSame(appendableMock, returned);
    }
}
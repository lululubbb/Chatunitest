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
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class CSVPrinter_15_5Test {

    private CSVPrinter csvPrinter;
    private Appendable appendable;

    @BeforeEach
    void setUp() throws Exception {
        appendable = new StringBuilder();
        csvPrinter = new CSVPrinter(appendable, CSVFormat.DEFAULT);
    }

    @Test
    @Timeout(8000)
    void testGetOutReturnsCorrectAppendable() {
        Appendable outFromMethod = csvPrinter.getOut();
        assertNotNull(outFromMethod);
        assertSame(appendable, outFromMethod);
    }

    @Test
    @Timeout(8000)
    void testGetOutWithReflectionAccess() throws Exception {
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outFieldValue = (Appendable) outField.get(csvPrinter);

        Appendable outFromMethod = csvPrinter.getOut();

        assertNotNull(outFieldValue);
        assertNotNull(outFromMethod);
        assertSame(outFieldValue, outFromMethod);
    }
}
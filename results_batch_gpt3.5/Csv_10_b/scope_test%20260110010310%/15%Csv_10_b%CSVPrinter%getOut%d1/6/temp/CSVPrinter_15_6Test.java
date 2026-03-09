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

import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVPrinter_15_6Test {

    private CSVPrinter csvPrinter;
    private Appendable appendable;

    @BeforeEach
    void setUp() throws Exception {
        appendable = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(appendable, format);
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsTheSameAppendableInstance() throws Exception {
        // Use reflection to get the private final field 'out'
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outFieldValue = (Appendable) outField.get(csvPrinter);

        // Call the focal method
        Appendable result = csvPrinter.getOut();

        // Assert that the returned instance is the same as the private field 'out'
        assertSame(outFieldValue, result);
    }
}
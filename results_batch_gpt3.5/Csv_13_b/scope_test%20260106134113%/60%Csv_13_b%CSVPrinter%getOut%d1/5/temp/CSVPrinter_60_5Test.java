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

class CSVPrinter_60_5Test {

    private Appendable appendable;
    private CSVFormat format;
    private CSVPrinter printer;

    @BeforeEach
    void setUp() throws Exception {
        appendable = new StringBuilder();
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(appendable, format);
    }

    @Test
    @Timeout(8000)
    void testGetOut_returnsSameAppendable() throws Exception {
        // Use reflection to access private final field 'out'
        Field outField = CSVPrinter.class.getDeclaredField("out");
        outField.setAccessible(true);
        Appendable outValue = (Appendable) outField.get(printer);

        Appendable result = printer.getOut();

        assertSame(outValue, result);
    }
}
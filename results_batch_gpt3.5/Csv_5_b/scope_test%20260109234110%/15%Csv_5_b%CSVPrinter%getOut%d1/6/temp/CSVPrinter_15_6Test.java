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

import java.lang.reflect.Constructor;

class CSVPrinter_15_6Test {

    @Test
    @Timeout(8000)
    void testGetOutReturnsSameAppendable() throws Exception {
        Appendable appendable = new StringBuilder();
        CSVFormat format = Mockito.mock(CSVFormat.class);

        // Use reflection to get the constructor and create instance
        Constructor<CSVPrinter> constructor = CSVPrinter.class.getDeclaredConstructor(Appendable.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVPrinter printer = constructor.newInstance(appendable, format);

        Appendable result = printer.getOut();

        assertSame(appendable, result, "getOut should return the same Appendable instance passed in constructor");
    }
}
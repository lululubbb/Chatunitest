package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_43_4Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreEmptyLinesNoArg() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withIgnoreEmptyLines();
        assertNotNull(result);
        assertTrue(result.getIgnoreEmptyLines());

        // Using reflection to invoke public method withIgnoreEmptyLines(boolean)
        Method method = CSVFormat.class.getMethod("withIgnoreEmptyLines", boolean.class);

        CSVFormat trueFormat = (CSVFormat) method.invoke(format, Boolean.TRUE);
        assertNotNull(trueFormat);
        assertTrue(trueFormat.getIgnoreEmptyLines());

        CSVFormat falseFormat = (CSVFormat) method.invoke(format, Boolean.FALSE);
        assertNotNull(falseFormat);
        assertFalse(falseFormat.getIgnoreEmptyLines());

        // Verify that withIgnoreEmptyLines() returns same as withIgnoreEmptyLines(true)
        CSVFormat directTrueFormat = format.withIgnoreEmptyLines();
        CSVFormat reflectedTrueFormat = (CSVFormat) method.invoke(format, Boolean.TRUE);
        assertEquals(directTrueFormat, reflectedTrueFormat);
    }
}
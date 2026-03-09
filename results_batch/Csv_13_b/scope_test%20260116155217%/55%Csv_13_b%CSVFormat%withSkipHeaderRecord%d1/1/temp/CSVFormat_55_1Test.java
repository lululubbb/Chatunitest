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

class CSVFormat_55_1Test {

    @Test
    @Timeout(8000)
    void testWithSkipHeaderRecord() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord());

        CSVFormat result = format.withSkipHeaderRecord();
        assertNotNull(result);
        assertTrue(result.getSkipHeaderRecord());

        // Using reflection to invoke public method withSkipHeaderRecord(boolean)
        Method method = CSVFormat.class.getMethod("withSkipHeaderRecord", boolean.class);
        CSVFormat reflectedResultFalse = (CSVFormat) method.invoke(format, false);
        assertNotNull(reflectedResultFalse);
        assertFalse(reflectedResultFalse.getSkipHeaderRecord());

        CSVFormat reflectedResultTrue = (CSVFormat) method.invoke(format, true);
        assertNotNull(reflectedResultTrue);
        assertTrue(reflectedResultTrue.getSkipHeaderRecord());

        // Check that original instance is not mutated
        assertFalse(format.getSkipHeaderRecord());
    }
}
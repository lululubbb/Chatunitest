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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_53_6Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Call the focal method with a char separator
        CSVFormat result = format.withRecordSeparator('\n');

        // The result should not be the same instance (immutable pattern)
        assertNotSame(format, result);

        // The record separator string should be "\n"
        assertEquals("\n", result.getRecordSeparator());

        // Also test with another char, e.g. '\r'
        CSVFormat result2 = format.withRecordSeparator('\r');
        assertEquals("\r", result2.getRecordSeparator());

        // Test with a non-line break character, e.g. 'a'
        CSVFormat result4 = format.withRecordSeparator('a');
        assertEquals("a", result4.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char_reflection() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get the public withRecordSeparator(String) method
        Method withRecordSeparatorString = CSVFormat.class.getMethod("withRecordSeparator", String.class);

        // Invoke withRecordSeparator(String) with a String "\n"
        CSVFormat invokedResult = (CSVFormat) withRecordSeparatorString.invoke(format, "\n");

        assertNotNull(invokedResult);
        assertEquals("\n", invokedResult.getRecordSeparator());
    }
}
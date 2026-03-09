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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_53_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;

        // Call the focal method with a char record separator
        CSVFormat result = original.withRecordSeparator('\n');

        // Verify that the returned CSVFormat is not the same instance (immutability)
        assertNotSame(original, result);

        // Verify that the record separator is set correctly
        String recordSeparator = result.getRecordSeparator();
        assertEquals("\n", recordSeparator);

        // Also test with a different char record separator
        CSVFormat result2 = original.withRecordSeparator('\r');
        assertEquals("\r", result2.getRecordSeparator());

        // Test with a non-linebreak char (e.g. comma)
        CSVFormat result3 = original.withRecordSeparator(',');
        assertEquals(",", result3.getRecordSeparator());
    }
}
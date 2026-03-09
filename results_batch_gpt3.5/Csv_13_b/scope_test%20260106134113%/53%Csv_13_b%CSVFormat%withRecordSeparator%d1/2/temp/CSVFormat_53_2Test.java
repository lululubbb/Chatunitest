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

class CSVFormat_53_2Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat original = CSVFormat.DEFAULT;
        char separator = '\n';
        CSVFormat updated = original.withRecordSeparator(separator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(String.valueOf(separator), updated.getRecordSeparator());

        // Also test that original is unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());

        // Test with a different char separator
        char sep2 = '\r';
        CSVFormat updated2 = original.withRecordSeparator(sep2);
        assertEquals(String.valueOf(sep2), updated2.getRecordSeparator());

        // Test with a non-line break char
        char sep3 = '|';
        CSVFormat updated3 = original.withRecordSeparator(sep3);
        assertEquals(String.valueOf(sep3), updated3.getRecordSeparator());
    }
}
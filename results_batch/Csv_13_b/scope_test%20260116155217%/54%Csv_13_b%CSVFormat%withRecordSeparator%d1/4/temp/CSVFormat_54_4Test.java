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

public class CSVFormat_54_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_String() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newSeparator = "\n";
        CSVFormat modified = original.withRecordSeparator(newSeparator);

        assertNotSame(original, modified, "withRecordSeparator should return a new instance");
        assertEquals(newSeparator, modified.getRecordSeparator(), "Record separator should be updated");
        // Verify original unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator(),
                "Original record separator should remain unchanged");

        // Test with null record separator
        CSVFormat nullSeparatorFormat = original.withRecordSeparator((String) null);
        assertNotSame(original, nullSeparatorFormat);
        assertNull(nullSeparatorFormat.getRecordSeparator(), "Record separator should be null");

        // Test with empty string separator
        CSVFormat emptySeparatorFormat = original.withRecordSeparator("");
        assertNotSame(original, emptySeparatorFormat);
        assertEquals("", emptySeparatorFormat.getRecordSeparator(), "Record separator should be empty string");
    }
}
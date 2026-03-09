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

class CSVFormat_54_2Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_String() {
        CSVFormat original = CSVFormat.DEFAULT;
        String newRecordSeparator = "\n";

        CSVFormat updated = original.withRecordSeparator(newRecordSeparator);

        assertNotNull(updated);
        assertNotSame(original, updated);
        assertEquals(newRecordSeparator, updated.getRecordSeparator());

        // Original remains unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());

        // Test with null record separator
        CSVFormat nullSeparatorFormat = original.withRecordSeparator((String) null);
        assertNotNull(nullSeparatorFormat);
        assertNotSame(original, nullSeparatorFormat);
        assertNull(nullSeparatorFormat.getRecordSeparator());
    }
}
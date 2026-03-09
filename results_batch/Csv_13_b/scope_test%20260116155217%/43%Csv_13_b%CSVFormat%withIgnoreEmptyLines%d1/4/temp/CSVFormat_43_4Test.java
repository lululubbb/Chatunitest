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

class CSVFormat_43_4Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreEmptyLines() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertTrue(original.getIgnoreEmptyLines()); // DEFAULT has ignoreEmptyLines = true

        CSVFormat updated = original.withIgnoreEmptyLines(false);
        assertNotNull(updated);
        assertFalse(updated.getIgnoreEmptyLines());

        // Ensure original remains unchanged (immutability)
        assertTrue(original.getIgnoreEmptyLines());

        // Calling withIgnoreEmptyLines() on updated again returns same or equivalent with false
        CSVFormat updatedAgain = updated.withIgnoreEmptyLines(false);
        assertNotNull(updatedAgain);
        assertFalse(updatedAgain.getIgnoreEmptyLines());

        // equals and hashCode consistency for updated formats
        assertEquals(updated.hashCode(), updatedAgain.hashCode());
        assertEquals(updated, updatedAgain);
    }
}
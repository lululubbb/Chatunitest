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

class CSVFormat_45_2Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreSurroundingSpaces(), "Default should not ignore surrounding spaces");

        CSVFormat updated = original.withIgnoreSurroundingSpaces(true);
        assertNotNull(updated, "Updated CSVFormat should not be null");
        assertTrue(updated.getIgnoreSurroundingSpaces(), "Updated CSVFormat should ignore surrounding spaces");

        // Ensure immutability: original instance unchanged
        assertFalse(original.getIgnoreSurroundingSpaces(), "Original CSVFormat should remain unchanged");

        // Also test withIgnoreSurroundingSpaces(boolean) with false
        CSVFormat reverted = updated.withIgnoreSurroundingSpaces(false);
        assertNotNull(reverted);
        assertFalse(reverted.getIgnoreSurroundingSpaces(), "Reverted CSVFormat should not ignore surrounding spaces");
    }
}
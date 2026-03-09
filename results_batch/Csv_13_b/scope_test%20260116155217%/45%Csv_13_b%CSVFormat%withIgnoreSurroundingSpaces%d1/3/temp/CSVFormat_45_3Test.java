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

class CSVFormat_45_3Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getIgnoreSurroundingSpaces(), "Default should not ignore surrounding spaces");

        CSVFormat updated = original.withIgnoreSurroundingSpaces(true);
        assertNotNull(updated);
        assertTrue(updated.getIgnoreSurroundingSpaces(), "Updated format should ignore surrounding spaces");

        // Calling again on updated should keep ignoreSurroundingSpaces true
        CSVFormat updatedAgain = updated.withIgnoreSurroundingSpaces(true);
        assertTrue(updatedAgain.getIgnoreSurroundingSpaces(), "Calling again should keep ignoreSurroundingSpaces true");

        // Ensure original instance is not modified (immutability)
        assertFalse(original.getIgnoreSurroundingSpaces(), "Original instance should remain unchanged");
    }
}
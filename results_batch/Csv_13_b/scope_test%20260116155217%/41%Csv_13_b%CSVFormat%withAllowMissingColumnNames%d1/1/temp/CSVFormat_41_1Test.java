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

class CSVFormat_41_1Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames() {
        CSVFormat original = CSVFormat.DEFAULT;
        assertFalse(original.getAllowMissingColumnNames(), "Default format should not allow missing column names");

        CSVFormat modified = original.withAllowMissingColumnNames(true);
        assertNotNull(modified, "Modified CSVFormat should not be null");
        assertTrue(modified.getAllowMissingColumnNames(), "Modified format should allow missing column names");

        // Ensure original instance is not modified (immutability)
        assertFalse(original.getAllowMissingColumnNames(), "Original CSVFormat should remain unchanged");

        // Calling withAllowMissingColumnNames again on modified should keep allowMissingColumnNames true
        CSVFormat modifiedAgain = modified.withAllowMissingColumnNames(true);
        assertTrue(modifiedAgain.getAllowMissingColumnNames(), "Repeated call should keep allowMissingColumnNames true");

        // Check that the returned instance has allowMissingColumnNames true
        assertEquals(modified.getAllowMissingColumnNames(), modifiedAgain.getAllowMissingColumnNames());
    }
}
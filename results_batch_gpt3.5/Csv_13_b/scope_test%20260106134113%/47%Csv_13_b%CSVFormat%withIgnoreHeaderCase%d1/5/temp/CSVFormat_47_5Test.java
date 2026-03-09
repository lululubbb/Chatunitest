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

public class CSVFormat_47_5Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreHeaderCase() {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat result = original.withIgnoreHeaderCase();

        assertNotNull(result);
        assertTrue(result.getIgnoreHeaderCase());
        // The original should remain unchanged (immutable)
        assertFalse(original.getIgnoreHeaderCase());
        // Calling again returns a new instance with ignoreHeaderCase = true
        CSVFormat result2 = result.withIgnoreHeaderCase();
        assertTrue(result2.getIgnoreHeaderCase());
    }
}
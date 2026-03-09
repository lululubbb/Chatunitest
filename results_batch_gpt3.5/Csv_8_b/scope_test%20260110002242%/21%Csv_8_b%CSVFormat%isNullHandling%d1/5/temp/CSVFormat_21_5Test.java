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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_21_5Test {

    @Test
    @Timeout(8000)
    void testIsNullHandlingWithNullStringNull() {
        // Create a new CSVFormat instance with nullString = null using withNullString()
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);

        // isNullHandling() should return false because nullString is null
        assertFalse(format.isNullHandling());
    }

    @Test
    @Timeout(8000)
    void testIsNullHandlingWithNullStringNonNull() {
        // Create a new CSVFormat instance with nullString = "NULL" using withNullString()
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // isNullHandling() should return true because nullString is non-null
        assertTrue(format.isNullHandling());
    }
}
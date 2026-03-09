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

class CSVFormat_22_1Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        // nullString is null in DEFAULT, so isNullStringSet() should return false
        assertFalse(format.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNotNull() {
        // Create a CSVFormat instance with nullString set to a non-null value using withNullString()
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // Now isNullStringSet() should return true
        assertTrue(format.isNullStringSet());
    }
}
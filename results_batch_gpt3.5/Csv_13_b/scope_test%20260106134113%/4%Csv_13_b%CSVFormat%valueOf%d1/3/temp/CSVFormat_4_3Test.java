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

class CSVFormat_4_3Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidPredefinedFormat() {
        CSVFormat expectedFormat = CSVFormat.DEFAULT;
        CSVFormat actualFormat = CSVFormat.valueOf("DEFAULT");
        assertEquals(expectedFormat, actualFormat);
    }

    @Test
    @Timeout(8000)
    void testValueOf_withAnotherValidPredefinedFormat() {
        CSVFormat expectedFormat = CSVFormat.RFC4180;
        CSVFormat actualFormat = CSVFormat.valueOf("RFC4180");
        assertEquals(expectedFormat, actualFormat);
    }

    @Test
    @Timeout(8000)
    void testValueOf_withNullFormat_shouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormat_shouldThrowIllegalArgumentException() {
        String invalidFormat = "INVALID_FORMAT";
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(invalidFormat));
    }
}
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

class CSVFormat_4_5Test {

    @Test
    @Timeout(8000)
    void testValueOf_withDefaultFormats() {
        // Test predefined formats by name with exact case as defined in Predefined enum
        assertEquals(CSVFormat.DEFAULT, CSVFormat.valueOf("DEFAULT"));
        assertEquals(CSVFormat.RFC4180, CSVFormat.valueOf("RFC4180"));
        assertEquals(CSVFormat.EXCEL, CSVFormat.valueOf("EXCEL"));
        assertEquals(CSVFormat.TDF, CSVFormat.valueOf("TDF"));
        assertEquals(CSVFormat.MYSQL, CSVFormat.valueOf("MYSQL"));
    }

    @Test
    @Timeout(8000)
    void testValueOf_caseInsensitive() {
        // The CSVFormat.valueOf method delegates to Predefined.valueOf which is case-sensitive
        // So valueOf is case-sensitive and will throw IllegalArgumentException on different case
        // Adjust test to expect exception for wrong case
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("default"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("rfc4180"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("excel"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("tdf"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("mysql"));
    }

    @Test
    @Timeout(8000)
    void testValueOf_invalidFormat_throwsIllegalArgumentException() {
        // Expect IllegalArgumentException for unknown format names and null
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("unknown_format"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}
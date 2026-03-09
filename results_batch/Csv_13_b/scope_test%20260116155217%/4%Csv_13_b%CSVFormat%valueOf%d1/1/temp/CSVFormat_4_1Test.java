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

class CSVFormat_4_1Test {

    @Test
    @Timeout(8000)
    void testValueOfWithDefaultFormats() {
        // Test known predefined formats with exact case as defined in CSVFormat.Predefined enum
        assertEquals(CSVFormat.DEFAULT, CSVFormat.valueOf("DEFAULT"));
        assertEquals(CSVFormat.RFC4180, CSVFormat.valueOf("RFC4180"));
        assertEquals(CSVFormat.EXCEL, CSVFormat.valueOf("EXCEL"));
        assertEquals(CSVFormat.TDF, CSVFormat.valueOf("TDF"));
        assertEquals(CSVFormat.MYSQL, CSVFormat.valueOf("MYSQL"));
    }

    @Test
    @Timeout(8000)
    void testValueOfCaseSensitive() {
        // Predefined.valueOf is case sensitive, so valueOf will throw IllegalArgumentException for other cases
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("default"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("excel"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("rfc4180"));
    }

    @Test
    @Timeout(8000)
    void testValueOfInvalidFormatThrowsException() {
        // When an unknown format is passed, it should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UnknownFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}
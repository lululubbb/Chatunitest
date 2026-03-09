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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_4_4Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidFormat() {
        CSVFormat expectedFormat = CSVFormat.DEFAULT;
        CSVFormat actual = CSVFormat.valueOf("DEFAULT");
        assertSame(expectedFormat, actual);
    }

    @Test
    @Timeout(8000)
    void testValueOf_withNullFormat_throwsException() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("INVALID_FORMAT"));
    }
}
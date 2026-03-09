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

class CSVFormatIsNullStringSetTest {

    private CSVFormat setNullStringField(CSVFormat original, String nullString) {
        return original.withNullString(nullString);
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = setNullStringField(format, null);

        assertFalse(newFormat.isNullStringSet());
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNonNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat newFormat = setNullStringField(format, "NULL");

        assertTrue(newFormat.isNullStringSet());
    }
}
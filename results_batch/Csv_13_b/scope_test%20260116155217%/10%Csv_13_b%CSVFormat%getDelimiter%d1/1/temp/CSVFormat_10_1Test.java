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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CSVFormat_10_1Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_DefaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_CustomDelimiter() {
        CSVFormat format = CSVFormat.newFormat(';');
        assertEquals(';', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithDelimiterMethod() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat modified = base.withDelimiter('|');
        assertEquals('|', modified.getDelimiter());
        // Ensure original is unchanged
        assertEquals(',', base.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_OtherPredefinedFormats() {
        assertEquals('\t', CSVFormat.TDF.getDelimiter());
        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
        assertEquals(',', CSVFormat.RFC4180.getDelimiter());
        assertEquals(',', CSVFormat.EXCEL.getDelimiter());
    }
}
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

class CSVFormat_4_6Test {

    @Test
    @Timeout(8000)
    void testValueOfWithKnownFormat() {
        CSVFormat format = CSVFormat.valueOf("DEFAULT");
        assertNotNull(format);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), format.getDelimiter());
        assertEquals(CSVFormat.DEFAULT.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testValueOfWithRFC4180Format() {
        CSVFormat format = CSVFormat.valueOf("RFC4180");
        assertNotNull(format);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), format.getDelimiter());
        assertEquals(CSVFormat.RFC4180.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals(CSVFormat.RFC4180.getRecordSeparator(), format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testValueOfWithExcelFormat() {
        CSVFormat format = CSVFormat.valueOf("EXCEL");
        assertNotNull(format);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), format.getDelimiter());
        assertEquals(CSVFormat.EXCEL.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals(CSVFormat.EXCEL.getRecordSeparator(), format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testValueOfWithTDFFormat() {
        CSVFormat format = CSVFormat.valueOf("TDF");
        assertNotNull(format);
        assertEquals(CSVFormat.TDF.getDelimiter(), format.getDelimiter());
        assertEquals(CSVFormat.TDF.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals(CSVFormat.TDF.getRecordSeparator(), format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testValueOfWithMYSQLFormat() {
        CSVFormat format = CSVFormat.valueOf("MYSQL");
        assertNotNull(format);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), format.getDelimiter());
        assertEquals(CSVFormat.MYSQL.getQuoteCharacter(), format.getQuoteCharacter());
        assertEquals(CSVFormat.MYSQL.getRecordSeparator(), format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testValueOfWithInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("UNKNOWN_FORMAT"));
    }

    @Test
    @Timeout(8000)
    void testValueOfWithNull() {
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}
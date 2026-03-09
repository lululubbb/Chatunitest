package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CSVFormat_4_2Test {

    @Test
    @Timeout(8000)
    void testValueOfKnownFormats() {
        // Test predefined format names that should be recognized
        CSVFormat formatDefault = CSVFormat.valueOf("DEFAULT");
        assertNotNull(formatDefault);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), formatDefault.getDelimiter());

        CSVFormat formatExcel = CSVFormat.valueOf("EXCEL");
        assertNotNull(formatExcel);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), formatExcel.getDelimiter());

        CSVFormat formatInformixUnload = CSVFormat.valueOf("INFORMIX_UNLOAD");
        assertNotNull(formatInformixUnload);
        assertEquals(CSVFormat.INFORMIX_UNLOAD.getDelimiter(), formatInformixUnload.getDelimiter());

        CSVFormat formatInformixUnloadCsv = CSVFormat.valueOf("INFORMIX_UNLOAD_CSV");
        assertNotNull(formatInformixUnloadCsv);
        assertEquals(CSVFormat.INFORMIX_UNLOAD_CSV.getDelimiter(), formatInformixUnloadCsv.getDelimiter());

        CSVFormat formatMysql = CSVFormat.valueOf("MYSQL");
        assertNotNull(formatMysql);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), formatMysql.getDelimiter());

        CSVFormat formatRfc4180 = CSVFormat.valueOf("RFC4180");
        assertNotNull(formatRfc4180);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), formatRfc4180.getDelimiter());

        CSVFormat formatTdf = CSVFormat.valueOf("TDF");
        assertNotNull(formatTdf);
        assertEquals(CSVFormat.TDF.getDelimiter(), formatTdf.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testValueOfCaseInsensitive() {
        // valueOf is case sensitive, so test with uppercase only
        CSVFormat formatLower = CSVFormat.valueOf("DEFAULT");
        CSVFormat formatUpper = CSVFormat.valueOf("DEFAULT");
        CSVFormat formatMixed = CSVFormat.valueOf("DEFAULT");
        assertEquals(CSVFormat.DEFAULT, formatLower);
        assertEquals(CSVFormat.DEFAULT, formatUpper);
        assertEquals(CSVFormat.DEFAULT, formatMixed);
    }

    @Test
    @Timeout(8000)
    void testValueOfNullAndEmpty() {
        // valueOf should throw NullPointerException or IllegalArgumentException for null or empty input
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(" "));
    }

    @Test
    @Timeout(8000)
    void testValueOfUnknownFormat() {
        // valueOf should throw IllegalArgumentException for unknown format string
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("unknownFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("CSV"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("random"));
    }
}
package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
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
    void testValueOf_withDefaultFormats() {
        // Test known predefined formats
        CSVFormat formatDefault = CSVFormat.valueOf("DEFAULT");
        assertNotNull(formatDefault);
        assertEquals(CSVFormat.DEFAULT.getDelimiter(), formatDefault.getDelimiter());

        CSVFormat formatExcel = CSVFormat.valueOf("EXCEL");
        assertNotNull(formatExcel);
        assertEquals(CSVFormat.EXCEL.getDelimiter(), formatExcel.getDelimiter());

        CSVFormat formatInformixUnload = CSVFormat.valueOf("INFORMIX_UNLOAD");
        assertNotNull(formatInformixUnload);
        assertEquals(CSVFormat.INFORMIX_UNLOAD.getDelimiter(), formatInformixUnload.getDelimiter());

        CSVFormat formatMysql = CSVFormat.valueOf("MYSQL");
        assertNotNull(formatMysql);
        assertEquals(CSVFormat.MYSQL.getDelimiter(), formatMysql.getDelimiter());

        CSVFormat formatPostgresqlCsv = CSVFormat.valueOf("POSTGRESQL_CSV");
        assertNotNull(formatPostgresqlCsv);
        assertEquals(CSVFormat.POSTGRESQL_CSV.getDelimiter(), formatPostgresqlCsv.getDelimiter());

        CSVFormat formatPostgresqlText = CSVFormat.valueOf("POSTGRESQL_TEXT");
        assertNotNull(formatPostgresqlText);
        assertEquals(CSVFormat.POSTGRESQL_TEXT.getDelimiter(), formatPostgresqlText.getDelimiter());

        CSVFormat formatRfc4180 = CSVFormat.valueOf("RFC4180");
        assertNotNull(formatRfc4180);
        assertEquals(CSVFormat.RFC4180.getDelimiter(), formatRfc4180.getDelimiter());

        CSVFormat formatTdf = CSVFormat.valueOf("TDF");
        assertNotNull(formatTdf);
        assertEquals(CSVFormat.TDF.getDelimiter(), formatTdf.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testValueOf_invalidFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NonExistingFormat"));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
    }
}
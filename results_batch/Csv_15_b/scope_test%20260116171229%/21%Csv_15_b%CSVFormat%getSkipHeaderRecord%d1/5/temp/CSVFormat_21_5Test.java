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

class CSVFormat_21_5Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordDefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord(), "DEFAULT format should not skip header record");
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordTrueViaWithSkipHeaderRecord() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord(), "Format withSkipHeaderRecord(true) should skip header record");
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordFalseViaWithSkipHeaderRecord() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord(), "Format withSkipHeaderRecord(false) should not skip header record");
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordOnPredefinedFormats() {
        assertFalse(CSVFormat.EXCEL.getSkipHeaderRecord());
        assertFalse(CSVFormat.INFORMIX_UNLOAD.getSkipHeaderRecord());
        assertFalse(CSVFormat.INFORMIX_UNLOAD_CSV.getSkipHeaderRecord());
        assertFalse(CSVFormat.MYSQL.getSkipHeaderRecord());
        assertFalse(CSVFormat.POSTGRESQL_CSV.getSkipHeaderRecord());
        assertFalse(CSVFormat.POSTGRESQL_TEXT.getSkipHeaderRecord());
        assertFalse(CSVFormat.RFC4180.getSkipHeaderRecord());
        assertFalse(CSVFormat.TDF.getSkipHeaderRecord());
    }
}
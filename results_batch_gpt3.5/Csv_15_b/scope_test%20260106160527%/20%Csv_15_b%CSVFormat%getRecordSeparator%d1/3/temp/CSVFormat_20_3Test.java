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

class CSVFormat_20_3Test {

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_InformixUnload() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD;
        assertEquals("\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_InformixUnloadCsv() {
        CSVFormat format = CSVFormat.INFORMIX_UNLOAD_CSV;
        assertEquals("\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_MySQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals("\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_PostgreSQLCsv() {
        CSVFormat format = CSVFormat.POSTGRESQL_CSV;
        assertEquals("\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_PostgreSQLText() {
        CSVFormat format = CSVFormat.POSTGRESQL_TEXT;
        assertEquals("\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Rfc4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Tdf() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_CustomFormat() {
        CSVFormat customFormat = CSVFormat.newFormat(';').withRecordSeparator("\r");
        assertEquals("\r", customFormat.getRecordSeparator());
    }

}
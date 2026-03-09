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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_8_4Test {

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNames() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames();
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    void testGetAllowMissingColumnNames_ConstantFormats() {
        assertFalse(CSVFormat.DEFAULT.getAllowMissingColumnNames());
        assertTrue(CSVFormat.EXCEL.getAllowMissingColumnNames());
        assertFalse(CSVFormat.INFORMIX_UNLOAD.getAllowMissingColumnNames());
        assertFalse(CSVFormat.INFORMIX_UNLOAD_CSV.getAllowMissingColumnNames());
        assertFalse(CSVFormat.MYSQL.getAllowMissingColumnNames());
        assertFalse(CSVFormat.POSTGRESQL_CSV.getAllowMissingColumnNames());
        assertFalse(CSVFormat.POSTGRESQL_TEXT.getAllowMissingColumnNames());
        assertFalse(CSVFormat.RFC4180.getAllowMissingColumnNames());
        assertFalse(CSVFormat.TDF.getAllowMissingColumnNames());
    }
}
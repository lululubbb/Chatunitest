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

class CSVFormat_22_2Test {

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_Default() {
        // DEFAULT has trailingDelimiter = false
        assertFalse(CSVFormat.DEFAULT.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_ConstantFormats() {
        // Check trailingDelimiter for predefined constants
        assertFalse(CSVFormat.EXCEL.getTrailingDelimiter());
        assertFalse(CSVFormat.INFORMIX_UNLOAD.getTrailingDelimiter());
        assertFalse(CSVFormat.INFORMIX_UNLOAD_CSV.getTrailingDelimiter());
        assertFalse(CSVFormat.MYSQL.getTrailingDelimiter());
        assertFalse(CSVFormat.POSTGRESQL_CSV.getTrailingDelimiter());
        assertFalse(CSVFormat.POSTGRESQL_TEXT.getTrailingDelimiter());
        assertFalse(CSVFormat.RFC4180.getTrailingDelimiter());
        assertFalse(CSVFormat.TDF.getTrailingDelimiter());
    }
}
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

import java.lang.reflect.Field;

class CSVFormat_22_3Test {

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_Default() throws Exception {
        // Default constant DEFAULT has trailingDelimiter = false
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        // No need to forcibly set the private field since withTrailingDelimiter sets it internally
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        assertTrue(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_WithTrailingDelimiterFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetTrailingDelimiter_OtherConstants() throws Exception {
        // EXCEL inherits from DEFAULT, trailingDelimiter should be false
        CSVFormat excelFormat = CSVFormat.EXCEL.withTrailingDelimiter(false);
        assertFalse(excelFormat.getTrailingDelimiter());

        // INFORMIX_UNLOAD inherits from DEFAULT, trailingDelimiter should be false
        CSVFormat informixFormat = CSVFormat.INFORMIX_UNLOAD.withTrailingDelimiter(false);
        assertFalse(informixFormat.getTrailingDelimiter());

        // MYSQL inherits from DEFAULT, trailingDelimiter should be false
        CSVFormat mysqlFormat = CSVFormat.MYSQL.withTrailingDelimiter(false);
        assertFalse(mysqlFormat.getTrailingDelimiter());

        // POSTGRESQL_CSV inherits from DEFAULT, trailingDelimiter should be false
        CSVFormat postgresFormat = CSVFormat.POSTGRESQL_CSV.withTrailingDelimiter(false);
        assertFalse(postgresFormat.getTrailingDelimiter());

        // TDF inherits from DEFAULT, trailingDelimiter should be false
        CSVFormat tdfFormat = CSVFormat.TDF.withTrailingDelimiter(false);
        assertFalse(tdfFormat.getTrailingDelimiter());
    }
}
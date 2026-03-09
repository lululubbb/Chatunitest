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

class CSVFormat_45_2Test {

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getAllowMissingColumnNames(), "Default CSVFormat should not allow missing column names");
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_True() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames(), "CSVFormat withAllowMissingColumnNames(true) should allow missing column names");
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_False() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames(), "CSVFormat withAllowMissingColumnNames(false) should not allow missing column names");
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_ThenFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true).withAllowMissingColumnNames(false);
        assertFalse(format.getAllowMissingColumnNames(), "CSVFormat withAllowMissingColumnNames(true) then withAllowMissingColumnNames(false) should not allow missing column names");
    }

    @Test
    @Timeout(8000)
    void testWithAllowMissingColumnNames_ThenTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false).withAllowMissingColumnNames(true);
        assertTrue(format.getAllowMissingColumnNames(), "CSVFormat withAllowMissingColumnNames(false) then withAllowMissingColumnNames(true) should allow missing column names");
    }
}
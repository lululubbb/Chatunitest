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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

class CSVFormat_15_4Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_Default() {
        // The default CSVFormat.DEFAULT has ignoreHeaderCase = false
        assertFalse(CSVFormat.DEFAULT.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        assertTrue(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        assertFalse(format.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_ConstantFormats() {
        // EXCEL has ignoreHeaderCase true because it's derived from DEFAULT withIgnoreEmptyLines(false).withAllowMissingColumnNames()
        assertTrue(CSVFormat.EXCEL.getIgnoreHeaderCase());

        // INFORMIX_UNLOAD inherits from DEFAULT, ignoreHeaderCase false
        assertFalse(CSVFormat.INFORMIX_UNLOAD.getIgnoreHeaderCase());

        // INFORMIX_UNLOAD_CSV inherits from DEFAULT, ignoreHeaderCase false
        assertFalse(CSVFormat.INFORMIX_UNLOAD_CSV.getIgnoreHeaderCase());

        // MYSQL inherits from DEFAULT, ignoreHeaderCase false
        assertFalse(CSVFormat.MYSQL.getIgnoreHeaderCase());

        // RFC4180 inherits from DEFAULT, ignoreHeaderCase false
        assertFalse(CSVFormat.RFC4180.getIgnoreHeaderCase());

        // TDF inherits from DEFAULT, ignoreHeaderCase false
        assertFalse(CSVFormat.TDF.getIgnoreHeaderCase());
    }
}
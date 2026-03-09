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

class CSVFormat_15_2Test {

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_Default() {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        assertFalse(csvFormat.getIgnoreHeaderCase(), "DEFAULT should have ignoreHeaderCase = false");
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseTrue() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        assertTrue(csvFormat.getIgnoreHeaderCase(), "withIgnoreHeaderCase(true) should set ignoreHeaderCase = true");
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_WithIgnoreHeaderCaseFalse() {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreHeaderCase(false);
        assertFalse(csvFormat.getIgnoreHeaderCase(), "withIgnoreHeaderCase(false) should set ignoreHeaderCase = false");
    }

    @Test
    @Timeout(8000)
    void testGetIgnoreHeaderCase_ConstantFormats() {
        assertFalse(CSVFormat.EXCEL.getIgnoreHeaderCase(), "EXCEL should have ignoreHeaderCase = false");
        assertFalse(CSVFormat.INFORMIX_UNLOAD.getIgnoreHeaderCase(), "INFORMIX_UNLOAD should have ignoreHeaderCase = false");
        assertFalse(CSVFormat.INFORMIX_UNLOAD_CSV.getIgnoreHeaderCase(), "INFORMIX_UNLOAD_CSV should have ignoreHeaderCase = false");
        assertFalse(CSVFormat.MYSQL.getIgnoreHeaderCase(), "MYSQL should have ignoreHeaderCase = false");
        assertFalse(CSVFormat.RFC4180.getIgnoreHeaderCase(), "RFC4180 should have ignoreHeaderCase = false");
        assertFalse(CSVFormat.TDF.getIgnoreHeaderCase(), "TDF should have ignoreHeaderCase = false");
    }
}
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

public class CSVFormat_43_6Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_noArg() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withAllowMissingColumnNames();
        assertNotNull(result);
        assertTrue(result.getAllowMissingColumnNames());
        // The original instance should remain unchanged
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_booleanTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        CSVFormat result = format.withAllowMissingColumnNames(true);
        assertNotNull(result);
        assertTrue(result.getAllowMissingColumnNames());
        assertFalse(format.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_booleanFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat result = format.withAllowMissingColumnNames(false);
        assertNotNull(result);
        assertFalse(result.getAllowMissingColumnNames());
        assertTrue(format.getAllowMissingColumnNames());
    }
}
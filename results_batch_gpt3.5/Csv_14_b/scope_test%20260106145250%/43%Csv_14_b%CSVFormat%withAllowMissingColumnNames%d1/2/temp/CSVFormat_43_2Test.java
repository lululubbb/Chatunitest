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

public class CSVFormat_43_2Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_noArg() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames();

        assertNotNull(newFormat);
        // The returned CSVFormat should be different instance (immutable pattern)
        assertNotSame(baseFormat, newFormat);
        // The allowMissingColumnNames flag should be true in the new instance
        assertTrue(newFormat.getAllowMissingColumnNames());
        // The original instance should remain unchanged
        assertFalse(baseFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_true() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames(true);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertTrue(newFormat.getAllowMissingColumnNames());
        assertFalse(baseFormat.getAllowMissingColumnNames());
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_false() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        CSVFormat newFormat = baseFormat.withAllowMissingColumnNames(false);

        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertFalse(newFormat.getAllowMissingColumnNames());
        assertTrue(baseFormat.getAllowMissingColumnNames());
    }

}
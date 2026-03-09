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

public class CSVFormat_72_6Test {

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter_DefaultTrue() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        CSVFormat newFormat = baseFormat.withTrailingDelimiter();
        assertNotNull(newFormat);
        assertTrue(newFormat.getTrailingDelimiter());
        // Original should remain unchanged
        assertFalse(baseFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter_False() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat newFormat = baseFormat.withTrailingDelimiter(false);
        assertNotNull(newFormat);
        assertFalse(newFormat.getTrailingDelimiter());
        // Original should remain unchanged
        assertTrue(baseFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter_TrueWhenAlreadyTrue() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        CSVFormat newFormat = baseFormat.withTrailingDelimiter(true);
        // Should return the same instance if trailingDelimiter is unchanged
        assertSame(baseFormat, newFormat);
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter_FalseWhenAlreadyFalse() {
        CSVFormat baseFormat = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        CSVFormat newFormat = baseFormat.withTrailingDelimiter(false);
        // Should return the same instance if trailingDelimiter is unchanged
        assertSame(baseFormat, newFormat);
    }
}
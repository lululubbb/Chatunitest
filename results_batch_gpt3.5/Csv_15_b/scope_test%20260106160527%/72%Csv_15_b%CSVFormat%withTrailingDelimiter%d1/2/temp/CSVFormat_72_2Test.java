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

public class CSVFormat_72_2Test {

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter_defaultTrue() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat result = format.withTrailingDelimiter();
        assertNotNull(result);
        assertTrue(result.getTrailingDelimiter());
        // Original instance should be unchanged (immutability)
        assertFalse(format.getTrailingDelimiter());
        // The returned instance should be different if trailingDelimiter changes
        if (format.getTrailingDelimiter() != result.getTrailingDelimiter()) {
            assertNotSame(format, result);
        }
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter_false() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);
        assertNotNull(format);
        assertFalse(format.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithTrailingDelimiter_true() {
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);
        assertNotNull(format);
        assertTrue(format.getTrailingDelimiter());
    }
}
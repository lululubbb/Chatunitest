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

class CSVFormat_70_2Test {

    @Test
    @Timeout(8000)
    void testWithTrailingDelimiter() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;
        // Call the no-arg withTrailingDelimiter(), should enable trailingDelimiter
        CSVFormat formatWithTrailing = baseFormat.withTrailingDelimiter();
        assertNotNull(formatWithTrailing);
        assertTrue(formatWithTrailing.getTrailingDelimiter());
        // The original format should remain unchanged
        assertFalse(baseFormat.getTrailingDelimiter());

        // Call withTrailingDelimiter(false) to disable trailingDelimiter
        CSVFormat formatWithoutTrailing = formatWithTrailing.withTrailingDelimiter(false);
        assertNotNull(formatWithoutTrailing);
        assertFalse(formatWithoutTrailing.getTrailingDelimiter());

        // Call withTrailingDelimiter(true) again to enable trailingDelimiter
        CSVFormat formatWithTrailingAgain = formatWithoutTrailing.withTrailingDelimiter(true);
        assertNotNull(formatWithTrailingAgain);
        assertTrue(formatWithTrailingAgain.getTrailingDelimiter());
    }
}
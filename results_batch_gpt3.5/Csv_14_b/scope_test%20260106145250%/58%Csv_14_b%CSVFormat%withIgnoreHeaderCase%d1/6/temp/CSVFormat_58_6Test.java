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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormatWithIgnoreHeaderCaseTest {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreHeaderCase());

        CSVFormat formatWithIgnore = format.withIgnoreHeaderCase(true);

        assertNotNull(formatWithIgnore);
        assertTrue(formatWithIgnore.getIgnoreHeaderCase());

        // Original format should remain unchanged (immutable pattern)
        assertFalse(format.getIgnoreHeaderCase());

        // Calling withIgnoreHeaderCase again on the returned instance should keep ignoreHeaderCase true
        CSVFormat formatWithIgnoreAgain = formatWithIgnore.withIgnoreHeaderCase(true);
        assertTrue(formatWithIgnoreAgain.getIgnoreHeaderCase());
    }
}
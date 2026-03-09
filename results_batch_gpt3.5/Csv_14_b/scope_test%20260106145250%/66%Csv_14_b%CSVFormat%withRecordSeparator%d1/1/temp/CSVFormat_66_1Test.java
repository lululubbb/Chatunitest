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

class CSVFormat_66_1Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorChar() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test with common separators
        CSVFormat formatCR = baseFormat.withRecordSeparator('\r');
        assertNotNull(formatCR);
        assertEquals("\r", formatCR.getRecordSeparator());

        CSVFormat formatLF = baseFormat.withRecordSeparator('\n');
        assertNotNull(formatLF);
        assertEquals("\n", formatLF.getRecordSeparator());

        CSVFormat formatCRLF = baseFormat.withRecordSeparator("\r\n");
        assertNotNull(formatCRLF);
        assertEquals("\r\n", formatCRLF.getRecordSeparator());

        // Test with non-line break character
        CSVFormat formatPipe = baseFormat.withRecordSeparator('|');
        assertNotNull(formatPipe);
        assertEquals("|", formatPipe.getRecordSeparator());

        // Test with whitespace character
        CSVFormat formatSpace = baseFormat.withRecordSeparator(' ');
        assertNotNull(formatSpace);
        assertEquals(" ", formatSpace.getRecordSeparator());

        // Test with tab character
        CSVFormat formatTab = baseFormat.withRecordSeparator('\t');
        assertNotNull(formatTab);
        assertEquals("\t", formatTab.getRecordSeparator());

        // Test that original format remains unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());
    }
}
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

public class CSVFormat_21_1Test {

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord_DefaultFalse() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getSkipHeaderRecord(), "DEFAULT skipHeaderRecord should be false");
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord_WithTrue() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        assertTrue(format.getSkipHeaderRecord(), "skipHeaderRecord should be true after withSkipHeaderRecord(true)");
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord_WithFalse() {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        assertFalse(format.getSkipHeaderRecord(), "skipHeaderRecord should be false after withSkipHeaderRecord(false)");
    }

    @Test
    @Timeout(8000)
    public void testGetSkipHeaderRecord_Immutability() {
        CSVFormat format1 = CSVFormat.DEFAULT;
        CSVFormat format2 = format1.withSkipHeaderRecord(true);
        CSVFormat format3 = format2.withSkipHeaderRecord(false);

        assertFalse(format1.getSkipHeaderRecord(), "Original format should remain unchanged");
        assertTrue(format2.getSkipHeaderRecord(), "Format2 should have skipHeaderRecord true");
        assertFalse(format3.getSkipHeaderRecord(), "Format3 should have skipHeaderRecord false");
    }
}
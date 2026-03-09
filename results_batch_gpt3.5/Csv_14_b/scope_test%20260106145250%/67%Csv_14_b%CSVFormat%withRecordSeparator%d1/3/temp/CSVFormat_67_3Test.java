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

class CSVFormat_67_3Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparatorString() {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Test changing record separator to a new value (String)
        String newSeparator = "\n";
        CSVFormat updatedFormat = baseFormat.withRecordSeparator(newSeparator);
        assertNotNull(updatedFormat);
        assertEquals(newSeparator, updatedFormat.getRecordSeparator());
        // Original format remains unchanged
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), baseFormat.getRecordSeparator());

        // Test changing record separator to empty string
        CSVFormat emptySepFormat = baseFormat.withRecordSeparator("");
        assertNotNull(emptySepFormat);
        assertEquals("", emptySepFormat.getRecordSeparator());

        // Test changing record separator to null
        CSVFormat nullSepFormat = baseFormat.withRecordSeparator((String) null);
        assertNotNull(nullSepFormat);
        assertNull(nullSepFormat.getRecordSeparator());

        // Test chaining withRecordSeparator calls:
        // withRecordSeparator(char) exists, so call that first, then withRecordSeparator(String)
        CSVFormat chainFormat = baseFormat.withRecordSeparator('\r').withRecordSeparator("\r\n");
        assertEquals("\r\n", chainFormat.getRecordSeparator());
    }
}
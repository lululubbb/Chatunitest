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

public class CSVFormat_3_1Test {

    @Test
    @Timeout(8000)
    public void testNewFormat() {
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.newFormat(delimiter);

        assertNotNull(csvFormat);
        assertEquals(delimiter, csvFormat.getDelimiter());
        assertNull(csvFormat.getQuoteCharacter());
        assertNull(csvFormat.getQuoteMode());
        assertNull(csvFormat.getCommentMarker());
        assertNull(csvFormat.getEscapeCharacter());
        assertFalse(csvFormat.getIgnoreSurroundingSpaces());
        assertFalse(csvFormat.getIgnoreEmptyLines());
        assertNull(csvFormat.getRecordSeparator());
        assertNull(csvFormat.getNullString());
        assertNull(csvFormat.getHeaderComments());
        assertNull(csvFormat.getHeader());
        assertFalse(csvFormat.getSkipHeaderRecord());
        assertFalse(csvFormat.getAllowMissingColumnNames());
        assertFalse(csvFormat.getIgnoreHeaderCase());
        assertFalse(csvFormat.getTrim());
        assertFalse(csvFormat.getTrailingDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testEquals() {
        CSVFormat csvFormat1 = CSVFormat.newFormat(',');
        CSVFormat csvFormat2 = CSVFormat.newFormat(',');
        CSVFormat csvFormat3 = CSVFormat.newFormat('|');

        assertTrue(csvFormat1.equals(csvFormat2));
        assertFalse(csvFormat1.equals(csvFormat3));
    }

}
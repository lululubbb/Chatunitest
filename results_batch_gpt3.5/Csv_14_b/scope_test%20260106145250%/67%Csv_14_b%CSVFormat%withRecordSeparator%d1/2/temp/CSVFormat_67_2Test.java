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

class CSVFormat_67_2Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_String() {
        CSVFormat original = CSVFormat.DEFAULT;

        // Test with a normal record separator string
        String sep1 = "\n";
        CSVFormat format1 = original.withRecordSeparator(sep1);
        assertNotSame(original, format1);
        assertEquals(sep1, format1.getRecordSeparator());
        // Other properties should be equal
        assertEquals(original.getDelimiter(), format1.getDelimiter());
        assertEquals(original.getQuoteCharacter(), format1.getQuoteCharacter());
        assertEquals(original.getQuoteMode(), format1.getQuoteMode());
        assertEquals(original.getCommentMarker(), format1.getCommentMarker());
        assertEquals(original.getEscapeCharacter(), format1.getEscapeCharacter());
        assertEquals(original.getIgnoreSurroundingSpaces(), format1.getIgnoreSurroundingSpaces());
        assertEquals(original.getIgnoreEmptyLines(), format1.getIgnoreEmptyLines());
        assertEquals(original.getNullString(), format1.getNullString());
        assertArrayEquals(original.getHeaderComments(), format1.getHeaderComments());
        assertArrayEquals(original.getHeader(), format1.getHeader());
        assertEquals(original.getSkipHeaderRecord(), format1.getSkipHeaderRecord());
        assertEquals(original.getAllowMissingColumnNames(), format1.getAllowMissingColumnNames());
        assertEquals(original.getIgnoreHeaderCase(), format1.getIgnoreHeaderCase());
        assertEquals(original.getTrim(), format1.getTrim());
        assertEquals(original.getTrailingDelimiter(), format1.getTrailingDelimiter());

        // Test with empty string as record separator
        String sep2 = "";
        CSVFormat format2 = original.withRecordSeparator(sep2);
        assertEquals(sep2, format2.getRecordSeparator());

        // Test with null record separator
        CSVFormat format3 = original.withRecordSeparator((String) null);
        assertNull(format3.getRecordSeparator());

        // Test chaining: change record separator twice
        CSVFormat format4 = original.withRecordSeparator("\r\n").withRecordSeparator("\t");
        assertEquals("\t", format4.getRecordSeparator());
    }
}
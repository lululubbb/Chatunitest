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

class CSVFormat_66_5Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use a normal char as record separator
        char recordSeparator = '\n';
        CSVFormat newFormat = format.withRecordSeparator(recordSeparator);
        assertNotNull(newFormat);
        assertEquals(String.valueOf(recordSeparator), newFormat.getRecordSeparator());

        // Use a carriage return char
        recordSeparator = '\r';
        newFormat = format.withRecordSeparator(recordSeparator);
        assertEquals(String.valueOf(recordSeparator), newFormat.getRecordSeparator());

        // Use a non-linebreak char
        recordSeparator = 'X';
        newFormat = format.withRecordSeparator(recordSeparator);
        assertEquals("X", newFormat.getRecordSeparator());

        // Verify that original format is unchanged (immutability)
        assertEquals("\r\n", format.getRecordSeparator());
    }
}
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

class CSVFormat_68_5Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        // Call withRecordSeparator(char) with a normal character
        char separator = '\n';
        CSVFormat newFormat = baseFormat.withRecordSeparator(separator);
        assertNotNull(newFormat);
        assertNotSame(baseFormat, newFormat);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());

        // Call withRecordSeparator(char) with a special character (e.g. CR)
        separator = '\r';
        newFormat = baseFormat.withRecordSeparator(separator);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());

        // Call withRecordSeparator(char) with a multi-char line break (CRLF)
        // Since method converts char to String, only first char is used
        separator = '\r';
        newFormat = baseFormat.withRecordSeparator(separator);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());

        // Test with non-printable character
        separator = 0;
        newFormat = baseFormat.withRecordSeparator(separator);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());

        // Test with space character
        separator = ' ';
        newFormat = baseFormat.withRecordSeparator(separator);
        assertEquals(String.valueOf(separator), newFormat.getRecordSeparator());
    }
}
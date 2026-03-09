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

import java.lang.reflect.Field;

class CSVFormat_68_4Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() throws Exception {
        CSVFormat baseFormat = CSVFormat.DEFAULT;

        char recordSeparator = '\n';
        CSVFormat updatedFormat = baseFormat.withRecordSeparator(recordSeparator);

        assertNotNull(updatedFormat);
        assertNotSame(baseFormat, updatedFormat);
        assertEquals(String.valueOf(recordSeparator), updatedFormat.getRecordSeparator());

        // Use reflection to access Constants.CRLF value
        Class<?> constantsClass = Class.forName("org.apache.commons.csv.Constants");
        Field crlfField = constantsClass.getField("CRLF");
        String CRLF = (String) crlfField.get(null);

        // check that original format is unchanged
        assertEquals(CRLF, baseFormat.getRecordSeparator());

        // test with another char
        char rs2 = '\r';
        CSVFormat updatedFormat2 = baseFormat.withRecordSeparator(rs2);
        assertEquals(String.valueOf(rs2), updatedFormat2.getRecordSeparator());

        // test with an unusual char
        char rs3 = '|';
        CSVFormat updatedFormat3 = baseFormat.withRecordSeparator(rs3);
        assertEquals(String.valueOf(rs3), updatedFormat3.getRecordSeparator());
    }
}
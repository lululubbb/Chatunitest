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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_21_3Test {

    private CSVFormat csvFormat;

    @BeforeEach
    void setup() {
        csvFormat = CSVFormat.newFormat(',').withQuote('"').withRecordSeparator("\r\n")
                .withIgnoreEmptyLines(false).withIgnoreSurroundingSpaces(true);
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord() {
        // Given
        boolean expected = false;
        
        // When
        boolean result = csvFormat.getSkipHeaderRecord();
        
        // Then
        assertEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecordAfterSetting() {
        // Given
        boolean expected = true;
        csvFormat = csvFormat.withSkipHeaderRecord(true);

        // When
        boolean result = csvFormat.getSkipHeaderRecord();

        // Then
        assertEquals(expected, result);
    }
}
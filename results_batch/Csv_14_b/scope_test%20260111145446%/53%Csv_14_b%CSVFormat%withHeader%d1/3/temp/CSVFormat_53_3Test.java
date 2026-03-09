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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CSVFormat_53_3Test {

    @Mock
    private ResultSetMetaData metaData;

    @Test
    @Timeout(8000)
    public void testWithHeader() throws SQLException {
        // Mocking ResultSetMetaData
        when(metaData.getColumnCount()).thenReturn(3);
        when(metaData.getColumnLabel(1)).thenReturn("Column1");
        when(metaData.getColumnLabel(2)).thenReturn("Column2");
        when(metaData.getColumnLabel(3)).thenReturn("Column3");

        // Creating CSVFormat instance
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote(Constants.DOUBLE_QUOTE_CHAR)
                .withRecordSeparator(Constants.CRLF)
                .withIgnoreEmptyLines(false)
                .withAllowMissingColumnNames()
                .withIgnoreSurroundingSpaces(true);

        // Invoking withHeader method
        CSVFormat result = csvFormat.withHeader(metaData);

        // Verifying the result
        assertArrayEquals(new String[]{"Column1", "Column2", "Column3"}, result.getHeader());
    }

    // Add more test cases for other methods if needed
}
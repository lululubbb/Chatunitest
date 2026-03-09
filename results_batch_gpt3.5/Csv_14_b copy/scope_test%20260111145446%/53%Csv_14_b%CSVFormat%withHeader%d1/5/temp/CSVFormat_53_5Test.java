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
public class CSVFormat_53_5Test {

    @Mock
    ResultSetMetaData metaData;

    @Test
    @Timeout(8000)
    public void testWithHeader() throws SQLException {
        // Mock ResultSetMetaData
        int columnCount = 3;
        when(metaData.getColumnCount()).thenReturn(columnCount);
        when(metaData.getColumnLabel(1)).thenReturn("Column1");
        when(metaData.getColumnLabel(2)).thenReturn("Column2");
        when(metaData.getColumnLabel(3)).thenReturn("Column3");

        // Create CSVFormat instance
        CSVFormat csvFormat = CSVFormat.newFormat(',');

        // Invoke withHeader method
        CSVFormat result = csvFormat.withHeader(metaData);

        // Verify the result
        String[] expectedLabels = {"Column1", "Column2", "Column3"};
        assertArrayEquals(expectedLabels, result.getHeader());
    }
}
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CSVFormat_53_4Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() throws SQLException {
        // Mock ResultSetMetaData
        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(metaData.getColumnCount()).thenReturn(3);
        Mockito.when(metaData.getColumnLabel(1)).thenReturn("Label1");
        Mockito.when(metaData.getColumnLabel(2)).thenReturn("Label2");
        Mockito.when(metaData.getColumnLabel(3)).thenReturn("Label3");

        // Create CSVFormat instance using reflection
        CSVFormat csvFormat = createCSVFormatInstance();

        // Call the method under test
        CSVFormat result = csvFormat.withHeader(metaData);

        // Verify the result
        String[] expectedLabels = {"Label1", "Label2", "Label3"};
        assertArrayEquals(expectedLabels, result.getHeader());
    }

    private CSVFormat createCSVFormatInstance() {
        try {
            return CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class,
                    Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class,
                    String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class)
                    .newInstance(',', '"', null, null, null, false, true, "\r\n",
                            null, null, null, false, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
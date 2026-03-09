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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CSVFormat_53_1Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() throws SQLException {
        // Create a mock ResultSetMetaData
        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(metaData.getColumnCount()).thenReturn(3);
        Mockito.when(metaData.getColumnLabel(1)).thenReturn("Column1");
        Mockito.when(metaData.getColumnLabel(2)).thenReturn("Column2");
        Mockito.when(metaData.getColumnLabel(3)).thenReturn("Column3");

        // Call the withHeader method
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat result = null;
        try {
            result = (CSVFormat) csvFormat.getClass().getMethod("withHeader", ResultSetMetaData.class).invoke(csvFormat, metaData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Verify the result
        assertNotNull(result);
        assertEquals("Column1", result.getHeader()[0]);
        assertEquals("Column2", result.getHeader()[1]);
        assertEquals("Column3", result.getHeader()[2]);
    }
}
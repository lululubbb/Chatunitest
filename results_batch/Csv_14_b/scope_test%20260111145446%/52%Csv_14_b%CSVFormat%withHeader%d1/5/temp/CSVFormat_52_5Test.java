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

import static org.apache.commons.csv.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CSVFormat_52_5Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() throws SQLException {
        // Create a mock ResultSet
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);

        // Define the behavior of the mock objects
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);

        // Create an instance of CSVFormat
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Call the focal method
        CSVFormat result = csvFormat.withHeader(resultSet);

        // Validate the result
        assertEquals(csvFormat, result);
    }
}
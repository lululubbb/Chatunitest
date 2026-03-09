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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_40_3Test {

    @Test
    @Timeout(8000)
    public void testToStringArray() throws Exception {
        // Create a mock CSVFormat object
        CSVFormat csvFormat = mock(CSVFormat.class);

        // Mock input values
        Object[] values = { "value1", 2, null, "value4" };

        // Mock the private method toStringArray
        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);
        when(method.invoke(csvFormat, values)).thenCallRealMethod();

        // Invoke the private method using reflection
        String[] result = (String[]) method.invoke(csvFormat, (Object) values);

        // Expected output
        String[] expected = { "value1", "2", null, "value4" };

        // Assert the result
        assertArrayEquals(expected, result);
    }
}
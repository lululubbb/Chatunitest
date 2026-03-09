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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class CSVFormat_40_1Test {

    @Test
    @Timeout(8000)
    public void testToStringArray() throws Exception {
        // Create a mock CSVFormat object
        CSVFormat csvFormat = mock(CSVFormat.class);

        // Mock input values
        Object[] values = { "value1", "value2", null, "value4" };

        // Mock the private method call
        when(csvFormat.toStringArray(values)).thenCallRealMethod();

        // Invoke the private method using reflection
        String[] result = invokePrivateMethod(csvFormat, "toStringArray", values);

        // Expected output
        String[] expected = { "value1", "value2", null, "value4" };

        // Assert the result
        assertArrayEquals(expected, result);
    }

    private String[] invokePrivateMethod(CSVFormat csvFormat, String methodName, Object... args)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = CSVFormat.class.getDeclaredMethod(methodName, Object[].class);
        method.setAccessible(true);
        return (String[]) method.invoke(csvFormat, new Object[] { args });
    }
}
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
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_12_3Test {

    @Test
    @Timeout(8000)
    public void testGetHeader() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Create CSVFormat instance
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // Set header using reflection
        String[] header = {"Header1", "Header2"};
        Method setHeaderMethod = CSVFormat.class.getDeclaredMethod("withHeader", String[].class);
        setHeaderMethod.setAccessible(true);
        setHeaderMethod.invoke(csvFormat, (Object) header);

        // Invoke getHeader method using reflection
        Method getHeaderMethod = CSVFormat.class.getDeclaredMethod("getHeader");
        getHeaderMethod.setAccessible(true);
        String[] result = (String[]) getHeaderMethod.invoke(csvFormat);

        // Assert the result
        assertArrayEquals(header, result);
    }
}
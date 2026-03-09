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

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_22_2Test {

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_DefaultFalse() throws Exception {
        // Using DEFAULT CSVFormat which has trailingDelimiter = false
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to invoke getTrailingDelimiter()
        Method method = CSVFormat.class.getMethod("getTrailingDelimiter");
        boolean value = (boolean) method.invoke(format);

        assertFalse(value);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_True() throws Exception {
        // Create a CSVFormat instance with trailingDelimiter = true using withTrailingDelimiter(true)
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(true);

        // Use reflection to invoke getTrailingDelimiter()
        Method method = CSVFormat.class.getMethod("getTrailingDelimiter");
        boolean value = (boolean) method.invoke(format);

        assertTrue(value);
    }

    @Test
    @Timeout(8000)
    public void testGetTrailingDelimiter_FalseExplicit() throws Exception {
        // Create CSVFormat instance with trailingDelimiter = false explicitly
        CSVFormat format = CSVFormat.DEFAULT.withTrailingDelimiter(false);

        // Use reflection to invoke getTrailingDelimiter()
        Method method = CSVFormat.class.getMethod("getTrailingDelimiter");
        boolean value = (boolean) method.invoke(format);

        assertFalse(value);
    }
}
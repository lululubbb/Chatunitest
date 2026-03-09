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

public class CSVFormat_8_4Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_Default() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        boolean value = invokeGetAllowMissingColumnNames(format);
        assertFalse(value);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNames() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        boolean value = invokeGetAllowMissingColumnNames(format);
        assertTrue(value);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(false);
        boolean value = invokeGetAllowMissingColumnNames(format);
        assertFalse(value);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_WithAllowMissingColumnNamesTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withAllowMissingColumnNames(true);
        boolean value = invokeGetAllowMissingColumnNames(format);
        assertTrue(value);
    }

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames_ExcelConstant() throws Exception {
        boolean value = invokeGetAllowMissingColumnNames(CSVFormat.EXCEL);
        assertTrue(value);
    }

    private boolean invokeGetAllowMissingColumnNames(CSVFormat format) throws Exception {
        Method method = CSVFormat.class.getMethod("getAllowMissingColumnNames");
        return (boolean) method.invoke(format);
    }
}
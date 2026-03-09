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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_20_3Test {

    @Test
    @Timeout(8000)
    public void testGetRecordSeparator() {
        String expectedRecordSeparator = "\r\n";
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        String actualRecordSeparator = csvFormat.getRecordSeparator();

        assertEquals(expectedRecordSeparator, actualRecordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparatorForInformixUnload() {
        String expectedRecordSeparator = "\n";
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD;

        String actualRecordSeparator = csvFormat.getRecordSeparator();

        assertEquals(expectedRecordSeparator, actualRecordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testGetRecordSeparatorForInformixUnloadCsv() {
        String expectedRecordSeparator = "\n";
        CSVFormat csvFormat = CSVFormat.INFORMIX_UNLOAD_CSV;

        String actualRecordSeparator = csvFormat.getRecordSeparator();

        assertEquals(expectedRecordSeparator, actualRecordSeparator);
    }

    @Test
    @Timeout(8000)
    public void testAdditionalMethodsWithReflection() throws Exception {
        CSVFormat csvFormat = CSVFormat.EXCEL;

        boolean allowMissingColumnNames = (boolean) invokeMethod(csvFormat, "getAllowMissingColumnNames");
        boolean ignoreEmptyLines = (boolean) invokeMethod(csvFormat, "getIgnoreEmptyLines");
        boolean ignoreHeaderCase = (boolean) invokeMethod(csvFormat, "getIgnoreHeaderCase");
        boolean ignoreSurroundingSpaces = (boolean) invokeMethod(csvFormat, "getIgnoreSurroundingSpaces");
        boolean skipHeaderRecord = (boolean) invokeMethod(csvFormat, "getSkipHeaderRecord");
        boolean trailingDelimiter = (boolean) invokeMethod(csvFormat, "getTrailingDelimiter");
        boolean trim = (boolean) invokeMethod(csvFormat, "getTrim");

        assertEquals(false, allowMissingColumnNames);
        assertEquals(false, ignoreEmptyLines);
        assertEquals(false, ignoreHeaderCase);
        assertEquals(false, ignoreSurroundingSpaces);
        assertEquals(false, skipHeaderRecord);
        assertEquals(false, trailingDelimiter);
        assertEquals(true, trim);
    }

    private Object invokeMethod(CSVFormat csvFormat, String methodName) throws Exception {
        Method method = csvFormat.getClass().getMethod(methodName);
        return method.invoke(csvFormat);
    }
}
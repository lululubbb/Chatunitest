package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

class CSVFormat_27_3Test {

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNotNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        Method method = CSVFormat.class.getDeclaredMethod("isQuoteCharacterSet");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(format);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsQuoteCharacterSet_whenQuoteCharacterIsNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withQuote((Character) null);
        Method method = CSVFormat.class.getDeclaredMethod("isQuoteCharacterSet");
        method.setAccessible(true);
        boolean result = (boolean) method.invoke(format);
        assertFalse(result);
    }
}
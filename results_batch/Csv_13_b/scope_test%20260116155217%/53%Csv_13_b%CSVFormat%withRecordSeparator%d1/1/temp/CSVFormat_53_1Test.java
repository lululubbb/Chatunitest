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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class CSVFormat_53_1Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char separator = '\n';

        CSVFormat result = original.withRecordSeparator(separator);

        assertNotNull(result);
        assertNotSame(original, result);
        assertEquals(String.valueOf(separator), result.getRecordSeparator());

        // Use reflection to invoke public withRecordSeparator(String)
        Method method = CSVFormat.class.getMethod("withRecordSeparator", String.class);
        CSVFormat reflectedResult = (CSVFormat) method.invoke(original, String.valueOf(separator));
        assertEquals(result, reflectedResult);
    }
}
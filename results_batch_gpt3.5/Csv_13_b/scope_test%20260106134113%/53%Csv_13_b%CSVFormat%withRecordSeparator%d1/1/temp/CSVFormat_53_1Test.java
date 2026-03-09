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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_53_1Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() throws Exception {
        CSVFormat defaultFormat = CSVFormat.DEFAULT;

        // Call public withRecordSeparator(char)
        CSVFormat newFormat = defaultFormat.withRecordSeparator('\n');
        assertNotNull(newFormat);
        assertEquals("\n", newFormat.getRecordSeparator());
        assertNotSame(defaultFormat, newFormat);

        // Call withRecordSeparator with a different char
        CSVFormat newFormat2 = defaultFormat.withRecordSeparator('\r');
        assertNotNull(newFormat2);
        assertEquals("\r", newFormat2.getRecordSeparator());
        assertNotSame(defaultFormat, newFormat2);

        // Call withRecordSeparator with the same record separator as newFormat2
        CSVFormat newFormat3 = defaultFormat.withRecordSeparator('\r');
        assertEquals("\r", newFormat3.getRecordSeparator());

        // Use reflection to invoke public withRecordSeparator(String)
        Method withRecordSeparatorStringMethod = CSVFormat.class.getDeclaredMethod("withRecordSeparator", String.class);
        withRecordSeparatorStringMethod.setAccessible(true);

        CSVFormat reflectedFormat = (CSVFormat) withRecordSeparatorStringMethod.invoke(defaultFormat, "\n");
        assertNotNull(reflectedFormat);
        assertEquals("\n", reflectedFormat.getRecordSeparator());
        assertNotSame(defaultFormat, reflectedFormat);
    }
}
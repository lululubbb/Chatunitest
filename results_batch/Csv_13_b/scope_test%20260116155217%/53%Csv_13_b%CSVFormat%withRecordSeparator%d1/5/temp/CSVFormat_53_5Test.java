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

class CSVFormat_53_5Test {

    @Test
    @Timeout(8000)
    void testWithRecordSeparator_char() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        char sep = '\n';

        // Invoke the public withRecordSeparator(char) method
        CSVFormat updated = original.withRecordSeparator(sep);

        // Verify that the returned CSVFormat has the expected record separator string
        assertNotNull(updated);
        assertEquals(String.valueOf(sep), updated.getRecordSeparator());

        // Verify original is unchanged (immutability)
        assertEquals(CSVFormat.DEFAULT.getRecordSeparator(), original.getRecordSeparator());

        // Use reflection to invoke the public withRecordSeparator(String) method
        Method method = CSVFormat.class.getMethod("withRecordSeparator", String.class);
        CSVFormat reflectResult = (CSVFormat) method.invoke(original, String.valueOf(sep));

        assertNotNull(reflectResult);
        assertEquals(String.valueOf(sep), reflectResult.getRecordSeparator());
    }
}
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
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;

public class CSVFormat_42_3Test {

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_False() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withAllowMissingColumnNames(false);

        assertNotNull(updated);
        assertFalse(updated.getAllowMissingColumnNames());
        // Original instance remains unchanged
        assertFalse(original.getAllowMissingColumnNames());

        // Using reflection to invoke the public getter
        Method getAllowMissingColumnNamesMethod = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        getAllowMissingColumnNamesMethod.setAccessible(true);
        boolean allowMissing = (boolean) getAllowMissingColumnNamesMethod.invoke(updated);
        assertFalse(allowMissing);
    }

    @Test
    @Timeout(8000)
    public void testWithAllowMissingColumnNames_True() throws Exception {
        CSVFormat original = CSVFormat.DEFAULT;
        CSVFormat updated = original.withAllowMissingColumnNames(true);

        assertNotNull(updated);
        assertTrue(updated.getAllowMissingColumnNames());
        // Original instance remains unchanged
        assertFalse(original.getAllowMissingColumnNames());

        // Using reflection to invoke the public getter
        Method getAllowMissingColumnNamesMethod = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        getAllowMissingColumnNamesMethod.setAccessible(true);
        boolean allowMissing = (boolean) getAllowMissingColumnNamesMethod.invoke(updated);
        assertTrue(allowMissing);
    }
}
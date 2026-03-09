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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_11_3Test {

    @Test
    @Timeout(8000)
    public void testGetAllowMissingColumnNames() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Create CSVFormat instance
        CSVFormat csvFormat = new CSVFormat(',', '\"', null, null, null, false, true, "\r\n", null, null, false, true);

        // Set private field allowMissingColumnNames to true
        Method allowMissingColumnNamesSetter = CSVFormat.class.getDeclaredMethod("withAllowMissingColumnNames", boolean.class);
        allowMissingColumnNamesSetter.setAccessible(true);
        allowMissingColumnNamesSetter.invoke(csvFormat, true);

        // Invoke getAllowMissingColumnNames using reflection
        Method getAllowMissingColumnNamesMethod = CSVFormat.class.getDeclaredMethod("getAllowMissingColumnNames");
        getAllowMissingColumnNamesMethod.setAccessible(true);
        boolean result = (boolean) getAllowMissingColumnNamesMethod.invoke(csvFormat);

        // Assert the result
        assertEquals(true, result);
    }
}
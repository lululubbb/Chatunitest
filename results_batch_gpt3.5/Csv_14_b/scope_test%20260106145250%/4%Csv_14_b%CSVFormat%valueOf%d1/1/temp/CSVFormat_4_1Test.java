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
import org.junit.jupiter.api.Test;

class CSVFormat_4_1Test {

    @Test
    @Timeout(8000)
    void testValueOf_withValidPredefinedFormats() throws Exception {
        // Use reflection to access the Predefined enum inside CSVFormat
        Class<?> predefinedClass = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");

        // For each enum constant, get the name and test valueOf
        Object[] enumConstants = predefinedClass.getEnumConstants();

        for (Object enumConstant : enumConstants) {
            String name = ((Enum<?>) enumConstant).name();
            CSVFormat format = CSVFormat.valueOf(name);
            assertNotNull(format);

            // Access getFormat() method from enum constant to get expected CSVFormat instance
            CSVFormat expectedFormat = (CSVFormat) predefinedClass.getMethod("getFormat").invoke(enumConstant);

            assertEquals(expectedFormat.getDelimiter(), format.getDelimiter());
        }
    }

    @Test
    @Timeout(8000)
    void testValueOf_withInvalidFormat_shouldThrowException() {
        // Expect IllegalArgumentException when format name is invalid
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NonExistingFormat"));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
    }
}
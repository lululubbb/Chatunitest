package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class CSVFormat_4_5Test {

    @Test
    @Timeout(8000)
    @DisplayName("Test valueOf with valid predefined formats")
    void testValueOfValidFormats() throws Exception {
        // Use reflection to access the Predefined enum class inside CSVFormat
        Class<?> predefinedClass = Class.forName("org.apache.commons.csv.CSVFormat$Predefined");

        // For each predefined format name, get the enum constant and its CSVFormat instance
        String[] formatNames = {
                "DEFAULT",
                "EXCEL",
                "INFORMIX_UNLOAD",
                "MYSQL",
                "POSTGRESQL_CSV",
                "POSTGRESQL_TEXT",
                "RFC4180",
                "TDF"
        };

        for (String name : formatNames) {
            @SuppressWarnings("unchecked")
            Class<Enum> enumClass = (Class<Enum>) predefinedClass.asSubclass(Enum.class);
            Enum<?> enumConstant = Enum.valueOf(enumClass, name);
            // The enum has a getFormat() method returning CSVFormat
            CSVFormat expectedFormat = (CSVFormat) predefinedClass.getMethod("getFormat").invoke(enumConstant);

            CSVFormat actualFormat = CSVFormat.valueOf(name);

            assertNotNull(actualFormat, "Format should not be null for: " + name);
            assertEquals(expectedFormat.getDelimiter(), actualFormat.getDelimiter(), "Delimiter mismatch for: " + name);
        }
    }

    @Test
    @Timeout(8000)
    @DisplayName("Test valueOf with invalid format name throws IllegalArgumentException")
    void testValueOfInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf("NonExistentFormat"));
        assertThrows(NullPointerException.class, () -> CSVFormat.valueOf(null));
        assertThrows(IllegalArgumentException.class, () -> CSVFormat.valueOf(""));
    }
}
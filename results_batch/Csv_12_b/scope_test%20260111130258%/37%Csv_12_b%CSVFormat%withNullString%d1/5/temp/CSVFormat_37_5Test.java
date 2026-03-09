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

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVFormat_37_5Test {

    @Test
    @Timeout(8000)
    public void testWithNullString() {
        // Given
        String nullString = "NULL";
        CSVFormat csvFormat = createCSVFormat(',', '"', null, null, null, false, true, "\r\n",
                null, null, false, false);

        // When
        CSVFormat newCsvFormat = csvFormat.withNullString(nullString);

        // Then
        assertEquals(nullString, newCsvFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testDefaultFormatNotNull() {
        // Given
        String nullString = "NULL";

        // When
        CSVFormat defaultFormat = CSVFormat.DEFAULT.withNullString(nullString);

        // Then
        assertEquals(nullString, defaultFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testRFC4180FormatNotNull() {
        // Given
        String nullString = "NULL";

        // When
        CSVFormat rfc4180Format = CSVFormat.RFC4180.withNullString(nullString);

        // Then
        assertEquals(nullString, rfc4180Format.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testExcelFormatNotNull() {
        // Given
        String nullString = "NULL";

        // When
        CSVFormat excelFormat = CSVFormat.EXCEL.withNullString(nullString);

        // Then
        assertEquals(nullString, excelFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testTDFFormatNotNull() {
        // Given
        String nullString = "NULL";

        // When
        CSVFormat tdfFormat = CSVFormat.TDF.withNullString(nullString);

        // Then
        assertEquals(nullString, tdfFormat.getNullString());
    }

    @Test
    @Timeout(8000)
    public void testMySQLFormatNotNull() {
        // Given
        String nullString = "NULL";

        // When
        CSVFormat mySQLFormat = CSVFormat.MYSQL.withNullString(nullString);

        // Then
        assertEquals(nullString, mySQLFormat.getNullString());
    }

    private CSVFormat createCSVFormat(char delimiter, char quoteCharacter, QuoteMode quoteMode, Character commentMarker,
            Character escapeCharacter, boolean ignoreSurroundingSpaces, boolean ignoreEmptyLines, String recordSeparator,
            String nullString, String[] header, boolean skipHeaderRecord, boolean allowMissingColumnNames) {
        try {
            return (CSVFormat) CSVFormat.class.getDeclaredConstructors()[0].newInstance(delimiter, quoteCharacter, quoteMode,
                    commentMarker, escapeCharacter, ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString,
                    header, skipHeaderRecord, allowMissingColumnNames);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
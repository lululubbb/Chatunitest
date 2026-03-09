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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CSVFormat_51_1Test {

    @Test
    @Timeout(8000)
    public void testWithHeader() {
        // Given
        CSVFormat csvFormat = CSVFormat.newFormat(',')
                .withQuote('"')
                .withIgnoreSurroundingSpaces(false)
                .withIgnoreEmptyLines(true)
                .withRecordSeparator("\r\n")
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(true)
                .withTrim(false)
                .withHeaderComments((Object[]) null)
                .withHeader((String[]) null)
                .withCommentMarker(null)
                .withDelimiter(',')
                .withEscape(null)
                .withQuoteMode(null)
                .withRecordSeparator(null)
                .withNullString(null)
                .withSkipHeaderRecord(false)
                .withAllowMissingColumnNames(false)
                .withIgnoreHeaderCase(false)
                .withTrim(false)
                .withTrailingDelimiter(false);

        Class<? extends Enum<?>> headerEnum = TestEnum.class;

        // When
        CSVFormat result = csvFormat.withHeader(headerEnum);

        // Then
        assertNotNull(result);
        assertEquals(TestEnum.values().length, result.getHeader().length);
        for (int i = 0; i < TestEnum.values().length; i++) {
            assertEquals(TestEnum.values()[i].name(), result.getHeader()[i]);
        }
    }

    private enum TestEnum {
        HEADER1, HEADER2, HEADER3
    }
}
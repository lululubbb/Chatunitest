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
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CSVFormat_37_1Test {

    @Test
    @Timeout(8000)
    public void testPrintln() throws IOException {
        // Given
        Appendable out = mock(Appendable.class);
        CSVFormat csvFormat = CSVFormat.newFormat('|').withQuote('"').withDelimiter('#').withEscape('\\')
                .withRecordSeparator("\r\n").withNullString("\\N").withHeader("Header1", "Header2")
                .withHeaderComments("Comment1", "Comment2").withTrailingDelimiter(true).withSkipHeaderRecord(false)
                .withIgnoreEmptyLines(true).withIgnoreHeaderCase(false).withIgnoreSurroundingSpaces(false)
                .withTrim(true);

        // When
        csvFormat.println(out);

        // Then
        verify(out).append('#');
        verify(out).append("\r\n");
    }
}
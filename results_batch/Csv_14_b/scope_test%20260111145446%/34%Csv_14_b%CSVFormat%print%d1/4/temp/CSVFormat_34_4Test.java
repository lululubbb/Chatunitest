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
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.StringWriter;

public class CSVFormat_34_4Test {

    @Test
    @Timeout(8000)
    public void testPrint() throws IOException {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        Object object = null;
        CharSequence value = "test";
        int offset = 0;
        int len = value.length();
        StringWriter out = new StringWriter();
        boolean newRecord = true;

        // When
        try {
            spyFormat.print(object, out, value, offset, len, newRecord);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // Then
        assertEquals("test", out.toString());
    }
}
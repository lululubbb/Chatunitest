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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_9_5Test {

    @Test
    @Timeout(8000)
    public void testGetCommentMarkerWhenNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getCommentMarker());
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarkerWhenSet() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        Character commentMarker = format.getCommentMarker();
        assertNotNull(commentMarker);
        assertEquals(Character.valueOf('#'), commentMarker);
    }

    @Test
    @Timeout(8000)
    public void testGetCommentMarkerWithNullCharacter() {
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker((Character) null);
        assertNull(format.getCommentMarker());
    }
}
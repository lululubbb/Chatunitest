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
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormat_20_4Test {

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet() {
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withCommentMarker('#').withIgnoreEmptyLines(true);
        boolean isCommentMarkerSet = csvFormat.isCommentMarkerSet();
        assertTrue(isCommentMarkerSet);
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_NotSet() {
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withIgnoreEmptyLines(true);
        boolean isCommentMarkerSet = csvFormat.isCommentMarkerSet();
        assertFalse(isCommentMarkerSet);
    }

    @Test
    @Timeout(8000)
    public void testIsCommentMarkerSet_WithMock() throws Exception {
        CSVFormat csvFormat = CSVFormat.newFormat(',').withQuote('"').withCommentMarker('#').withIgnoreEmptyLines(true);
        CSVFormat spyFormat = Mockito.spy(csvFormat);
        Mockito.when(spyFormat.getCommentMarker()).thenReturn('#');

        Method method = CSVFormat.class.getDeclaredMethod("isCommentMarkerSet");
        method.setAccessible(true);
        boolean isCommentMarkerSet = (boolean) method.invoke(spyFormat);

        assertTrue(isCommentMarkerSet);
    }
}
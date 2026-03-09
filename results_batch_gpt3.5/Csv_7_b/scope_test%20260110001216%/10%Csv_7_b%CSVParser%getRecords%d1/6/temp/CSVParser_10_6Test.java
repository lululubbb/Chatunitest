package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class CSVParser_10_6Test {

    private CSVParser parserMock;
    private CSVFormat formatMock;
    private Reader readerMock;

    @BeforeEach
    public void setup() {
        formatMock = mock(CSVFormat.class);
        readerMock = mock(Reader.class);
        parserMock = Mockito.spy(new CSVParser(readerMock, formatMock));
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_noArg_callsGetRecordsWithList() throws Exception {
        // Arrange
        List<CSVRecord> expectedList = new ArrayList<>();
        doReturn(expectedList).when(parserMock).getRecords(any());

        // Act
        List<CSVRecord> actual = parserMock.getRecords();

        // Assert
        assertSame(expectedList, actual);
        verify(parserMock).getRecords(any());
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withList_argumentAndReturn() throws Exception {
        // Arrange
        List<CSVRecord> records = new ArrayList<>();
        // Use reflection to invoke private getRecords(List)
        Method getRecordsListMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsListMethod.setAccessible(true);

        // Act
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsListMethod.invoke(parserMock, records);

        // Assert
        assertNotNull(result);
        assertSame(records, result);
    }

    @Test
    @Timeout(8000)
    public void testGetRecords_withList_reflectInvokePrivate_getRecords() throws Exception {
        // Arrange
        List<CSVRecord> inputList = new ArrayList<>();

        // Setup the lexer mock to simulate token reading.
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Lexer lexerMock = mock(Lexer.class);
        lexerField.set(parserMock, lexerMock);

        Field reusableTokenField = CSVParser.class.getDeclaredField("reusableToken");
        reusableTokenField.setAccessible(true);

        Class<?> tokenClass = Class.forName("org.apache.commons.csv.Token");
        Object tokenMock = mock(tokenClass);
        reusableTokenField.set(parserMock, tokenMock);

        Class<?> tokenTypeClass = Class.forName("org.apache.commons.csv.Token$Type");
        Object tokenTypeTOKEN = Enum.valueOf((Class<Enum>) tokenTypeClass, "TOKEN");
        Object tokenTypeEOF = Enum.valueOf((Class<Enum>) tokenTypeClass, "EOF");

        // Create a spy of tokenMock so we can stub getType()
        Object tokenSpy = Mockito.spy(tokenMock);
        reusableTokenField.set(parserMock, tokenSpy);

        // Stub getType() method on tokenSpy to return TOKEN then EOF using reflection
        Method getTypeMethod = tokenClass.getMethod("getType");
        org.mockito.stubbing.Answer<Object> answer = new org.mockito.stubbing.Answer<Object>() {
            private int count = 0;

            @Override
            public Object answer(org.mockito.invocation.InvocationOnMock invocation) {
                if (count == 0) {
                    count++;
                    return tokenTypeTOKEN;
                } else {
                    return tokenTypeEOF;
                }
            }
        };
        doAnswer(answer).when(tokenSpy).getType();

        // Setup lexer.nextToken to do nothing (tokenSpy is reusedToken)
        Method nextTokenMethod = Lexer.class.getMethod("nextToken", tokenClass);
        doAnswer(invocation -> null).when(lexerMock).nextToken((Object) tokenSpy);

        // Act
        Method getRecordsListMethod = CSVParser.class.getDeclaredMethod("getRecords", List.class);
        getRecordsListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<CSVRecord> result = (List<CSVRecord>) getRecordsListMethod.invoke(parserMock, inputList);

        // Assert
        assertNotNull(result);
        assertSame(inputList, result);
    }
}
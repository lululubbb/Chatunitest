package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_1_1Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;

    @BeforeEach
    void setUp() {
        values = new String[]{"a", "b", "c"};
        mapping = new HashMap<>();
        mapping.put("first", 0);
        mapping.put("second", 1);
        mapping.put("third", 2);
        comment = "comment";
        recordNumber = 42L;
    }

    @Test
    @Timeout(8000)
    void testConstructorWithValuesAndMappingAndCommentAndRecordNumber() throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(
                String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        // Normal constructor usage
        CSVRecord record = constructor.newInstance(values, mapping, comment, recordNumber);
        assertNotNull(record);

        // Check fields via public getters
        assertEquals(comment, record.getComment());
        assertEquals(recordNumber, record.getRecordNumber());
        assertEquals("a", record.get(0));
        assertEquals("b", record.get("second"));
        assertEquals("a", record.get(EnumTest.VALUE));

        // Null values array defaults to EMPTY_STRING_ARRAY
        CSVRecord recordNullValues = constructor.newInstance(null, mapping, comment, recordNumber);
        assertEquals(0, recordNullValues.size());

        // Null mapping allowed (some methods may throw NPE if used)
        CSVRecord recordNullMapping = constructor.newInstance(values, null, comment, recordNumber);
        assertEquals("a", recordNullMapping.get(0));
        // get(String) should throw NPE if mapping is null and called with String
        assertThrows(NullPointerException.class, () -> recordNullMapping.get("first"));
    }

    enum EnumTest { VALUE }

    @Test
    @Timeout(8000)
    void testGetByEnum() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("a", record.get(EnumTest.VALUE));
    }

    @Test
    @Timeout(8000)
    void testGetByIndex() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("a", record.get(0));
        assertEquals("b", record.get(1));
        assertEquals("c", record.get(2));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(3));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> record.get(-1));
    }

    @Test
    @Timeout(8000)
    void testGetByName() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals("a", record.get("first"));
        assertEquals("b", record.get("second"));
        assertEquals("c", record.get("third"));
        assertThrows(IllegalArgumentException.class, () -> record.get("unknown"));
        assertThrows(NullPointerException.class, () -> {
            // disambiguate get(String) by casting null to String
            record.get((String) null);
        });
    }

    @Test
    @Timeout(8000)
    void testGetComment() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(comment, record.getComment());

        CSVRecord recordNullComment = new CSVRecord(values, mapping, null, recordNumber);
        assertNull(recordNullComment.getComment());
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(recordNumber, record.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isConsistent());

        String[] valuesInconsistent = new String[]{"a", "b"};
        CSVRecord recordInconsistent = new CSVRecord(valuesInconsistent, mapping, comment, recordNumber);
        assertFalse(recordInconsistent.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsMapped() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertTrue(record.isMapped("first"));
        assertFalse(record.isMapped("unknown"));
        assertFalse(record.isMapped(null));
    }

    @Test
    @Timeout(8000)
    void testIsSet() {
        String[] valsWithNull = new String[]{"a", null, "c"};
        CSVRecord record = new CSVRecord(valsWithNull, mapping, comment, recordNumber);
        assertTrue(record.isSet("first"));
        assertFalse(record.isSet("second"));
        assertFalse(record.isSet("unknown"));
        assertFalse(record.isSet(null));
    }

    @Test
    @Timeout(8000)
    void testIterator() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Iterator<String> it = record.iterator();
        List<String> iterated = new ArrayList<>();
        while (it.hasNext()) {
            iterated.add(it.next());
        }
        assertEquals(Arrays.asList(values), iterated);
    }

    @Test
    @Timeout(8000)
    void testPutIn() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = new HashMap<>();
        Map<String, String> returned = record.putIn(map);
        assertSame(map, returned);
        assertEquals(3, map.size());
        assertEquals("a", map.get("first"));
        assertEquals("b", map.get("second"));
        assertEquals("c", map.get("third"));
    }

    @Test
    @Timeout(8000)
    void testSize() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        assertEquals(values.length, record.size());

        CSVRecord recordEmpty = new CSVRecord(null, mapping, comment, recordNumber);
        assertEquals(0, recordEmpty.size());
    }

    @Test
    @Timeout(8000)
    void testToList() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Method toListMethod = CSVRecord.class.getDeclaredMethod("toList");
        toListMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toListMethod.invoke(record);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    void testToMap() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Map<String, String> map = record.toMap();
        assertEquals(3, map.size());
        assertEquals("a", map.get("first"));
        assertEquals("b", map.get("second"));
        assertEquals("c", map.get("third"));
    }

    @Test
    @Timeout(8000)
    void testToString() {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        String str = record.toString();
        assertTrue(str.contains("a"));
        assertTrue(str.contains("b"));
        assertTrue(str.contains("c"));
    }

    @Test
    @Timeout(8000)
    void testValuesMethod() throws Exception {
        CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] vals = (String[]) valuesMethod.invoke(record);
        assertArrayEquals(values, vals);
    }
}
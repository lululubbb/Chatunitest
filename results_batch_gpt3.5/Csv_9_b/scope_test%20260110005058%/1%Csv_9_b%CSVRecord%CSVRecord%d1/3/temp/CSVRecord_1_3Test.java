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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVRecord_1_3Test {

    private String[] values;
    private Map<String, Integer> mapping;
    private String comment;
    private long recordNumber;
    private CSVRecord csvRecord;

    @BeforeEach
    void setUp() {
        values = new String[] { "val1", "val2", "val3" };
        mapping = new HashMap<>();
        mapping.put("col1", 0);
        mapping.put("col2", 1);
        mapping.put("col3", 2);
        comment = "This is a comment";
        recordNumber = 123L;
        csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullValues_setsEmptyArray() throws Exception {
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);
        CSVRecord record = constructor.newInstance((Object) null, mapping, comment, recordNumber);

        Field valuesField = CSVRecord.class.getDeclaredField("values");
        valuesField.setAccessible(true);
        String[] internalValues = (String[]) valuesField.get(record);
        assertNotNull(internalValues);
        assertEquals(0, internalValues.length);
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_validIndex() {
        assertEquals("val1", csvRecord.get(0));
        assertEquals("val2", csvRecord.get(1));
        assertEquals("val3", csvRecord.get(2));
    }

    @Test
    @Timeout(8000)
    void testGetByIndex_invalidIndex() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(-1));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> csvRecord.get(3));
    }

    enum TestEnum {
        col1, col2
    }

    @Test
    @Timeout(8000)
    void testGetByEnum_existing() {
        assertEquals("val1", csvRecord.get(TestEnum.col1));
        assertEquals("val2", csvRecord.get(TestEnum.col2));
    }

    @Test
    @Timeout(8000)
    void testGetByEnum_nullEnum() {
        assertThrows(NullPointerException.class, () -> csvRecord.get((Enum<?>) null));
    }

    @Test
    @Timeout(8000)
    void testGetByName_existing() {
        assertEquals("val1", csvRecord.get("col1"));
        assertEquals("val2", csvRecord.get("col2"));
        assertEquals("val3", csvRecord.get("col3"));
    }

    @Test
    @Timeout(8000)
    void testGetByName_nonExisting() {
        assertThrows(IllegalArgumentException.class, () -> csvRecord.get("nonexistent"));
    }

    @Test
    @Timeout(8000)
    void testGetComment() {
        assertEquals(comment, csvRecord.getComment());
    }

    @Test
    @Timeout(8000)
    void testGetRecordNumber() {
        assertEquals(recordNumber, csvRecord.getRecordNumber());
    }

    @Test
    @Timeout(8000)
    void testIsConsistent_trueAndFalse() throws Exception {
        // consistent: all rows same length
        assertTrue(csvRecord.isConsistent());

        // Create inconsistent CSVRecord with different values length than mapping
        Constructor<CSVRecord> constructor = CSVRecord.class.getDeclaredConstructor(String[].class, Map.class, String.class, long.class);
        constructor.setAccessible(true);

        String[] shortValues = new String[] { "val1" };
        CSVRecord inconsistentRecord = constructor.newInstance(shortValues, mapping, comment, recordNumber);
        assertFalse(inconsistentRecord.isConsistent());
    }

    @Test
    @Timeout(8000)
    void testIsMapped_existingAndNonExisting() {
        assertTrue(csvRecord.isMapped("col1"));
        assertFalse(csvRecord.isMapped("nonexistent"));
        assertFalse(csvRecord.isMapped(null));
    }

    @Test
    @Timeout(8000)
    void testIsSet_existingAndNonExisting() {
        assertTrue(csvRecord.isSet("col1"));
        assertFalse(csvRecord.isSet("nonexistent"));
        assertFalse(csvRecord.isSet(null));
    }

    @Test
    @Timeout(8000)
    void testIterator() {
        Iterator<String> it = csvRecord.iterator();
        List<String> iterated = new ArrayList<>();
        while (it.hasNext()) {
            iterated.add(it.next());
        }
        assertEquals(Arrays.asList(values), iterated);
    }

    @Test
    @Timeout(8000)
    void testPutIn() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> result = csvRecord.putIn(map);
        assertSame(map, result);
        assertEquals("val1", map.get("col1"));
        assertEquals("val2", map.get("col2"));
        assertEquals("val3", map.get("col3"));
    }

    @Test
    @Timeout(8000)
    void testSize() {
        assertEquals(values.length, csvRecord.size());
    }

    @Test
    @Timeout(8000)
    void testToList() throws Exception {
        Method toList = CSVRecord.class.getDeclaredMethod("toList");
        toList.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<String> list = (List<String>) toList.invoke(csvRecord);
        assertEquals(Arrays.asList(values), list);
    }

    @Test
    @Timeout(8000)
    void testToMap() {
        Map<String, String> map = csvRecord.toMap();
        assertEquals(values.length, map.size());
        for (Map.Entry<String, Integer> entry : mapping.entrySet()) {
            assertEquals(values[entry.getValue()], map.get(entry.getKey()));
        }
    }

    @Test
    @Timeout(8000)
    void testToString_containsValues() {
        String str = csvRecord.toString();
        for (String val : values) {
            assertTrue(str.contains(val));
        }
        if (comment != null) {
            assertTrue(str.contains(comment));
        }
        assertTrue(str.contains(Long.toString(recordNumber)));
    }

    @Test
    @Timeout(8000)
    void testValuesReflection() throws Exception {
        Method valuesMethod = CSVRecord.class.getDeclaredMethod("values");
        valuesMethod.setAccessible(true);
        String[] returnedValues = (String[]) valuesMethod.invoke(csvRecord);
        assertArrayEquals(values, returnedValues);
    }
}
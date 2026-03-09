package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectionHelper_111_6Test {

  static class TestRecord {
    public int x;
  }

  static class NonRecord {
  }

  private Field recordHelperField;
  private Object originalRecordHelper;
  private int originalModifiers;

  @BeforeEach
  void setUp() throws Exception {
    recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Save original value to restore later
    originalRecordHelper = recordHelperField.get(null);

    // Save original modifiers
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    originalModifiers = recordHelperField.getModifiers();

    // Remove final modifier on RECORD_HELPER field
    modifiersField.setInt(recordHelperField, originalModifiers & ~Modifier.FINAL);
  }

  @AfterEach
  void tearDown() throws Exception {
    // Restore original RECORD_HELPER value and modifiers
    recordHelperField.set(null, originalRecordHelper);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, originalModifiers);
  }

  @Test
    @Timeout(8000)
  void isRecord_returnsTrue_whenRecordHelperReturnsTrue() throws Exception {
    Class<?> recordHelperType = recordHelperField.getType();

    // Create a Mockito mock of the concrete class RecordHelper
    Object mockRecordHelper = mock(recordHelperType);

    // Use reflection to get the isRecord method
    Method isRecordMethod = recordHelperType.getDeclaredMethod("isRecord", Class.class);
    isRecordMethod.setAccessible(true);

    // Stub the isRecord method on the mock to return true for TestRecord.class
    when(isRecordMethod.invoke(mockRecordHelper, TestRecord.class)).thenReturn(null);
    // The above won't work because invoke calls the real method, so instead use Mockito's when (on mock)
    // So use Mockito's when on the mock's isRecord method via reflection proxy:
    // To do this, use Mockito's 'when' on the mock's isRecord method:
    // Cast mockRecordHelper to the recordHelperType and use Mockito's when:
    // We can use Mockito's doReturn(true).when(mockRecordHelper).isRecord(TestRecord.class);
    // But since the method is not visible, use reflection to get the method and stub it:

    // Instead, use Mockito's 'doAnswer' to stub isRecord method:
    doAnswer(invocation -> {
      Class<?> arg = invocation.getArgument(0);
      return arg == TestRecord.class;
    }).when(mockRecordHelper).getClass()
      .getMethod("isRecord", Class.class)
      .invoke(mockRecordHelper, TestRecord.class);

    // The above is complicated; simpler is to use Mockito's Answer with a custom mock:
    // So recreate mock with Answer:

    mockRecordHelper = mock(recordHelperType, invocation -> {
      if ("isRecord".equals(invocation.getMethod().getName()) && invocation.getArguments().length == 1) {
        Class<?> arg = (Class<?>) invocation.getArgument(0);
        return arg == TestRecord.class;
      }
      return invocation.callRealMethod();
    });

    // Replace the RECORD_HELPER field with the mock
    recordHelperField.set(null, mockRecordHelper);

    boolean result = ReflectionHelper.isRecord(TestRecord.class);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isRecord_returnsFalse_whenRecordHelperReturnsFalse() throws Exception {
    Class<?> recordHelperType = recordHelperField.getType();

    Object mockRecordHelper = mock(recordHelperType, invocation -> {
      if ("isRecord".equals(invocation.getMethod().getName()) && invocation.getArguments().length == 1) {
        Class<?> arg = (Class<?>) invocation.getArgument(0);
        return arg == NonRecord.class ? false : true;
      }
      return invocation.callRealMethod();
    });

    recordHelperField.set(null, mockRecordHelper);

    boolean result = ReflectionHelper.isRecord(NonRecord.class);

    assertFalse(result);
  }
}
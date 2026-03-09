package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.protobuf.Descriptors.FileDescriptor;

public class Bag_64_6Test {

  private static Method getDescriptorMethod;

  @BeforeAll
  public static void setup() throws NoSuchMethodException, ClassNotFoundException {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    getDescriptorMethod = bagClass.getDeclaredMethod("getDescriptor");
    getDescriptorMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testGetDescriptor_notNull() throws InvocationTargetException, IllegalAccessException {
    Object result = getDescriptorMethod.invoke(null);
    assertNotNull(result);
    assertTrue(result instanceof FileDescriptor);
  }

  @Test
    @Timeout(8000)
  public void testGetDescriptor_consistentReturn() throws InvocationTargetException, IllegalAccessException {
    Object firstCall = getDescriptorMethod.invoke(null);
    Object secondCall = getDescriptorMethod.invoke(null);
    assertSame(firstCall, secondCall);
  }
}
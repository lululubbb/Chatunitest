package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ReflectionAccessFilterHelper_101_5Test {

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withAndroidType() throws Exception {
    // Use reflection to access private static method isAndroidType(String)
    Method method = ReflectionAccessFilterHelper.class.getDeclaredMethod("isAndroidType", String.class);
    method.setAccessible(true);

    // Simulate android.app.Activity class name (class may not exist in test environment)
    String androidClassName = "android.app.Activity";
    boolean androidTypeResult = (boolean) method.invoke(null, androidClassName);
    assertTrue(androidTypeResult);

    // Test isAnyPlatformType with a class whose name starts with "android."
    // Since we cannot create a class in the android package, test with a dynamic proxy ClassLoader
    Class<?> dummyAndroidClass = createDummyClassWithName(androidClassName);
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(dummyAndroidClass));
  }

  private static Class<?> createDummyClassWithName(String className) throws Exception {
    // Define a dummy class with the given name using a custom ClassLoader
    // This loader defines a class with the given name by generating minimal bytecode

    byte[] classBytes = generateMinimalClassBytes(className);

    ClassLoader loader = new ClassLoader(ReflectionAccessFilterHelperTest.class.getClassLoader()) {
      public Class<?> defineClass() {
        return defineClass(className, classBytes, 0, classBytes.length);
      }
    };
    return loader.getClass().getDeclaredMethod("defineClass").invoke(loader).getClass();
  }

  private static byte[] generateMinimalClassBytes(String className) {
    // Convert the class name to internal form
    String internalName = className.replace('.', '/');

    // Minimal valid class bytecode for an empty class
    // Using a simple pre-built byte array for a class named "A"
    // We will patch the internal name accordingly

    // This byte array corresponds to:
    // public class A {}
    // Compiled with javac, minimal class file

    // We'll build a minimal class file with the given name using ASM or manual bytecode.
    // Since we can't use external libs, use a prebuilt byte array for "A" and patch the name.

    byte[] baseClassBytes = new byte[] {
      (byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE, // magic
      0x00, 0x00, 0x00, 0x34, // version 52.0 (Java 8)
      0x00, 0x0D, // constant pool count 13
      // 1: Utf8 "A"
      0x01, 0x00, 0x01, 0x41,
      // 2: Class #1
      0x07, 0x00, 0x01,
      // 3: Utf8 "<init>"
      0x01, 0x00, 0x06, 0x3C, 0x69, 0x6E, 0x69, 0x74, 0x3E,
      // 4: Utf8 "()V"
      0x01, 0x00, 0x03, 0x28, 0x29, 0x56,
      // 5: Utf8 "Code"
      0x01, 0x00, 0x04, 0x43, 0x6F, 0x64, 0x65,
      // 6: NameAndType #3:#4
      0x0C, 0x00, 0x03, 0x00, 0x04,
      // 7: Methodref #2.#6
      0x0A, 0x00, 0x02, 0x00, 0x06,
      // 8: Class java/lang/Object
      0x07, 0x00, 0x09,
      // 9: Utf8 "java/lang/Object"
      0x01, 0x00, 0x10, 0x6A, 0x61, 0x76, 0x61, 0x2F, 0x6C, 0x61, 0x6E,
      0x67, 0x2F, 0x4F, 0x62, 0x6A, 0x65, 0x63, 0x74,
      // 10: Utf8 "java/lang/Object"
      0x01, 0x00, 0x10, 0x6A, 0x61, 0x76, 0x61, 0x2F, 0x6C, 0x61, 0x6E,
      0x67, 0x2F, 0x4F, 0x62, 0x6A, 0x65, 0x63, 0x74,
      // 11: Utf8 "<init>"
      0x01, 0x00, 0x06, 0x3C, 0x69, 0x6E, 0x69, 0x74, 0x3E,
      // 12: Utf8 "()V"
      0x01, 0x00, 0x03, 0x28, 0x29, 0x56,
      // 13: Utf8 "SourceFile"
      0x01, 0x00, 0x0A, 0x53, 0x6F, 0x75, 0x72, 0x63, 0x65, 0x46, 0x69,
      0x6C, 0x65,
      // class access flags: public super
      0x00, 0x21,
      // this class: #2
      0x00, 0x02,
      // super class: #8
      0x00, 0x08,
      // interfaces count
      0x00, 0x00,
      // fields count
      0x00, 0x00,
      // methods count
      0x00, 0x01,
      // method 1: public <init>()V
      0x00, 0x01, // access flags: public
      0x00, 0x03, // name_index: #3 <init>
      0x00, 0x04, // descriptor_index: #4 ()V
      0x00, 0x01, // attributes count: 1
      // attribute 1: Code
      0x00, 0x05, // attribute_name_index: #5 Code
      0x00, 0x00, 0x00, 0x11, // attribute_length: 17 bytes
      0x00, 0x01, // max_stack = 1
      0x00, 0x01, // max_locals = 1
      0x00, 0x05, // code_length = 5
      0x2A,       // aload_0
      (byte)0xB7, 0x00, 0x07, // invokespecial #7 Method java/lang/Object.<init>
      (byte)0xB1, // return
      0x00, 0x00, // exception_table_length
      0x00, 0x00, // attributes_count
      // attributes count for class
      0x00, 0x01,
      // SourceFile attribute
      0x00, 0x0D, // attribute_name_index: #13 SourceFile
      0x00, 0x00, 0x00, 0x02, // attribute_length = 2
      0x00, 0x01 // sourcefile_index = #1 (A)
    };

    // Patch the class name in constant pool #1 (Utf8) and #2 (Class)
    // #1 Utf8 string length is 1 for "A", need to replace with className length and bytes
    // #2 Class references #1, no change needed

    // Find offset of Utf8 string #1: after 10 bytes header + 1 byte tag + 2 bytes length = offset 10
    // Actually, from the start:
    // 4 bytes magic + 4 bytes version + 2 bytes constant pool count = 10 bytes
    // Then constant pool entries start

    // The first constant pool entry is at offset 10:
    // tag(1 byte) + length(2 bytes) + bytes(length)
    // So Utf8 string length is at offset 11-12, bytes start at 13

    int utf8LengthOffset = 11;
    int utf8BytesOffset = 13;

    byte[] nameBytes = internalName.getBytes();
    int nameLen = nameBytes.length;

    // Create a new byte array with adjusted length for Utf8 string
    // The original array is fixed length, so create new array with adjusted size

    int lengthDiff = nameLen - 1; // original length is 1 for "A"

    byte[] newClassBytes = new byte[baseClassBytes.length + lengthDiff];

    // Copy bytes before Utf8 length
    System.arraycopy(baseClassBytes, 0, newClassBytes, 0, utf8LengthOffset);

    // Write new length (2 bytes)
    newClassBytes[utf8LengthOffset] = (byte) ((nameLen >> 8) & 0xFF);
    newClassBytes[utf8LengthOffset + 1] = (byte) (nameLen & 0xFF);

    // Write new name bytes
    System.arraycopy(nameBytes, 0, newClassBytes, utf8BytesOffset, nameLen);

    // Copy remaining bytes after original Utf8 bytes (offset 14)
    int restSrcPos = utf8BytesOffset + 1; // original name length was 1 byte
    int restDestPos = utf8BytesOffset + nameLen;
    int restLength = baseClassBytes.length - restSrcPos;

    System.arraycopy(baseClassBytes, restSrcPos, newClassBytes, restDestPos, restLength);

    // Patch SourceFile attribute name index and sourcefile_index to a valid value or leave as is (not critical)

    return newClassBytes;
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withKotlinPackage() {
    Class<?> kotlinClass = KotlinDummy.class;
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withKotlinxPackage() throws Exception {
    Class<?> kotlinxClass;
    try {
      kotlinxClass = Class.forName("kotlinx.coroutines.CoroutineScope");
    } catch (ClassNotFoundException e) {
      kotlinxClass = createDummyClassWithName("kotlinx.coroutines.CoroutineScope");
    }
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinxClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withScalaPackage() throws Exception {
    Class<?> scalaClass;
    try {
      scalaClass = Class.forName("scala.collection.Seq");
    } catch (ClassNotFoundException e) {
      scalaClass = createDummyClassWithName("scala.collection.Seq");
    }
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(scalaClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withNonPlatformType() {
    Class<?> nonPlatformClass = ReflectionAccessFilterHelperTest.class;
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(nonPlatformClass));
  }

  // Dummy class to simulate kotlin package class
  static class KotlinDummy {
    @Override
    public String toString() {
      return "kotlin.Dummy";
    }

}
}
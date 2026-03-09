package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_101_2Test {

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withAndroidType() throws Exception {
    Class<?> androidClass = createDummyClass("android.dummy.Class");
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(androidClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withKotlinPackage() throws Exception {
    Class<?> kotlinClass = createDummyClass("kotlin.dummy.Class");
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withKotlinxPackage() throws Exception {
    Class<?> kotlinxClass = createDummyClass("kotlinx.dummy.Class");
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(kotlinxClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withScalaPackage() throws Exception {
    Class<?> scalaClass = createDummyClass("scala.dummy.Class");
    assertTrue(ReflectionAccessFilterHelper.isAnyPlatformType(scalaClass));
  }

  @Test
    @Timeout(8000)
  void testIsAnyPlatformType_withOtherClass() throws Exception {
    Class<?> otherClass = createDummyClass("java.lang.String");
    assertFalse(ReflectionAccessFilterHelper.isAnyPlatformType(otherClass));
  }

  /**
   * Dynamically creates a dummy class with the given fully qualified name.
   * The class will be empty and have the specified package.
   */
  private static Class<?> createDummyClass(String fullClassName) throws Exception {
    int lastDot = fullClassName.lastIndexOf('.');
    String packageName = (lastDot > 0) ? fullClassName.substring(0, lastDot) : "";
    String simpleName = fullClassName.substring(lastDot + 1);

    String classSource = "package " + packageName + ";\n" +
        "public class " + simpleName + " {\n" +
        "}";

    // Compile the class source in-memory and load it
    InMemoryJavaCompiler compiler = new InMemoryJavaCompiler();
    return compiler.compile(fullClassName, classSource);
  }

  /**
   * Simple in-memory Java compiler and class loader.
   */
  static class InMemoryJavaCompiler extends ClassLoader {
    private final javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
    private final javax.tools.StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, null, null);

    public Class<?> compile(String className, String sourceCode) throws Exception {
      try (java.io.ByteArrayOutputStream byteCodeStream = new java.io.ByteArrayOutputStream()) {
        javax.tools.SimpleJavaFileObject fileObject = new javax.tools.SimpleJavaFileObject(
            java.net.URI.create("string:///" + className.replace('.', '/') + javax.tools.JavaFileObject.Kind.SOURCE.extension),
            javax.tools.JavaFileObject.Kind.SOURCE) {
          @Override
          public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return sourceCode;
          }
        };

        javax.tools.JavaFileObject compiledFile = new javax.tools.SimpleJavaFileObject(
            java.net.URI.create("string:///" + className.replace('.', '/') + javax.tools.JavaFileObject.Kind.CLASS.extension),
            javax.tools.JavaFileObject.Kind.CLASS) {
          private final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();

          @Override
          public java.io.OutputStream openOutputStream() {
            return baos;
          }

          byte[] getBytes() {
            return baos.toByteArray();
          }
        };

        javax.tools.ForwardingJavaFileManager<javax.tools.StandardJavaFileManager> fileManager =
          new javax.tools.ForwardingJavaFileManager<>(stdFileManager) {
            @Override
            public javax.tools.JavaFileObject getJavaFileForOutput(javax.tools.JavaFileManager.Location location,
                String className, javax.tools.JavaFileObject.Kind kind, javax.tools.FileObject sibling) {
              return compiledFile;
            }
          };

        javax.tools.JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, java.util.Collections.singletonList(fileObject));
        Boolean success = task.call();
        if (success == null || !success) {
          throw new RuntimeException("Compilation failed for class " + className);
        }

        byte[] bytes = ((javax.tools.SimpleJavaFileObject) compiledFile).getCharContent(false).toString().getBytes();
        // Use bytes from compiledFile instead of source bytes:
        bytes = ((javax.tools.SimpleJavaFileObject) compiledFile).openInputStream().readAllBytes();

        byte[] classBytes = ((javax.tools.SimpleJavaFileObject) compiledFile).openInputStream().readAllBytes();

        // Actually get bytes from compiledFile's baos:
        classBytes = ((javax.tools.SimpleJavaFileObject) compiledFile).openOutputStream().toString().getBytes();

        classBytes = ((javax.tools.SimpleJavaFileObject) compiledFile).openOutputStream().toString().getBytes();

        classBytes = ((javax.tools.SimpleJavaFileObject) compiledFile).openOutputStream().toString().getBytes();

        classBytes = ((javax.tools.SimpleJavaFileObject) compiledFile).getClass().getDeclaredMethod("getBytes").invoke(compiledFile) instanceof byte[] ? (byte[]) ((javax.tools.SimpleJavaFileObject) compiledFile).getClass().getDeclaredMethod("getBytes").invoke(compiledFile) : null;

        if (classBytes == null) {
          throw new RuntimeException("Failed to get compiled bytes for class " + className);
        }

        return defineClass(className, classBytes, 0, classBytes.length);
      }
    }
  }
}
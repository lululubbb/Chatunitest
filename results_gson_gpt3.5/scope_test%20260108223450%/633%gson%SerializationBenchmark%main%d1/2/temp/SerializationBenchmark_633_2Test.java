package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SerializationBenchmark_633_2Test {

  @Test
    @Timeout(8000)
  public void testMain_invokesRunnerWithCorrectArguments() throws Exception {
    try (MockedStatic<NonUploadingCaliperRunner> runnerMock = mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = new String[] {"arg1", "arg2"};

      SerializationBenchmark.main(args);

      runnerMock.verify(() -> NonUploadingCaliperRunner.run(SerializationBenchmark.class, args));
    }
  }

  @Test
    @Timeout(8000)
  public void testSetUp_andTimeObjectSerialization_reflection() throws Exception {
    SerializationBenchmark benchmark = new SerializationBenchmark();

    // Use reflection to invoke private field 'pretty' setter
    var prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);
    prettyField.set(benchmark, true);

    // Invoke @BeforeExperiment annotated method setUp()
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);

    // Invoke public method timeObjectSerialization(int)
    benchmark.timeObjectSerialization(5);
  }
}
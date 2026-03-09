package com.google.gson.metrics;
import org.junit.jupiter.api.Timeout;
import com.google.caliper.BeforeExperiment;
import com.google.caliper.Param;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class SerializationBenchmark_633_3Test {

  @Test
    @Timeout(8000)
  void testMain_invokesRunnerWithCorrectArgs() throws Exception {
    try (MockedStatic<NonUploadingCaliperRunner> runnerMock = Mockito.mockStatic(NonUploadingCaliperRunner.class)) {
      String[] args = new String[] {"arg1", "arg2"};
      SerializationBenchmark.main(args);
      runnerMock.verify(() -> NonUploadingCaliperRunner.run(SerializationBenchmark.class, args));
    }
  }

  @Test
    @Timeout(8000)
  void testSetUp_andTimeObjectSerialization_reflection() throws Exception {
    SerializationBenchmark benchmark = new SerializationBenchmark();

    // Invoke private field 'pretty' via reflection and set it to true and false to cover branches
    var prettyField = SerializationBenchmark.class.getDeclaredField("pretty");
    prettyField.setAccessible(true);

    // set pretty to true
    prettyField.set(benchmark, true);
    invokeBeforeExperiment(benchmark);
    invokeTimeObjectSerialization(benchmark, 5);

    // set pretty to false
    prettyField.set(benchmark, false);
    invokeBeforeExperiment(benchmark);
    invokeTimeObjectSerialization(benchmark, 3);
  }

  private void invokeBeforeExperiment(SerializationBenchmark benchmark) throws Exception {
    Method setUpMethod = SerializationBenchmark.class.getDeclaredMethod("setUp");
    setUpMethod.setAccessible(true);
    setUpMethod.invoke(benchmark);
  }

  private void invokeTimeObjectSerialization(SerializationBenchmark benchmark, int reps) throws Exception {
    Method timeMethod = SerializationBenchmark.class.getDeclaredMethod("timeObjectSerialization", int.class);
    timeMethod.setAccessible(true);
    timeMethod.invoke(benchmark, reps);
  }
}
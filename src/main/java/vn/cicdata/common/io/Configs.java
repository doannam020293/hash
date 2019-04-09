package vn.cicdata.common.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import org.apache.spark.SparkConf;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by namdd9 on 06/10/2018
 *
 */
public class Configs implements Serializable {

  public static final String FILE_CONFIG_PATH = "configs_custom.json";
  private Properties properties = new Properties();

  public enum Env {
    DEVELOPMENT("development"), PRODUCTION("production");

    private static final Map<String, Env> NAME_2_ENV = new HashMap<>();

    static {
      for (Env e : Env.values()) {
        NAME_2_ENV.put(e.getName(), e);
      }
    }

    private String name;

    Env(String env) {
      this.name = env;
    }

    public String getName() {
      return name;
    }

    public static Env fromString(String envString) {
      Env e = NAME_2_ENV.get(envString);
      if (e == null) {
        throw new IllegalArgumentException("No such environment: " + envString + ", possible environments are " + Arrays.toString(Env.values()));
      } else {
        return e;
      }
    }
  }

  public static Configs create(Env env) {
    return new Configs(env);
  }

  public static Configs create(String envString) {
    Env env = Env.fromString(envString);
    return new Configs(env);
  }

  private Configs(Env env) {
    loadConfig(env);
  }

  private void loadConfig(Env env) {
    InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(FILE_CONFIG_PATH);
    JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream));
    JsonObject jsonRoot = new JsonParser().parse(jsonReader).getAsJsonObject();
    loadAllProps(jsonRoot, env, properties);
  }

  private void loadAllProps(JsonObject jsonObject, Env env, Properties p) {
    JsonObject envConfigs = jsonObject.getAsJsonObject(env.getName());
    JsonElement father = envConfigs.get("inherit");
    if (father != null) {
      loadAllProps(jsonObject, Env.fromString(father.getAsString()), p);
    }
    JsonObject configCurrentEnv = envConfigs.getAsJsonObject("config");
    loadConfigFrom(configCurrentEnv, p);
  }

  private void loadConfigFrom(JsonObject jsonObject, Properties p) {
    for (Map.Entry<String, JsonElement> e : jsonObject.entrySet()) {
      p.put(e.getKey(), e.getValue().getAsString());
    }
  }


  private Integer getConfigInteger(String propName) {
    return stringToInteger(properties.getProperty(propName));
  }

  private Long getConfigLong(String propName) {
    return stringToLong(properties.getProperty(propName));
  }

  public String getConfigString(String propName) {
    return properties.getProperty(propName);
  }

  private Integer stringToInteger(String token) {
    return Integer.parseInt(token);
  }

  private Long stringToLong(String token) {
    return Long.parseLong(token);
  }


  public SparkConf createSparkConfig() {
    SparkConf sparkConf = new SparkConf();
    // sparkConf.set("spark.extraListeners", "com.viettel.vtcc.dm.userstats.spark.listener.UserStatsListener");
    sparkConf.set("spark.kryoserializer.buffer.max", "64m");
    sparkConf.set("spark.streaming.unpersist", "true");
    sparkConf.set("spark.speculation", "true");
    sparkConf.set("spark.speculation.quantile", "0.8");
    sparkConf.set("spark.speculation.multiplier", "2");
    sparkConf.set("spark.locality.wait", "500");
    sparkConf.set("spark.cleaner.ttl", "86400");
    sparkConf.set("spark.akka.frameSize", "256");
    sparkConf.set("spark.akka.threads", "10");
    sparkConf.set("spark.shuffle.consolidateFiles", "true");
    sparkConf.set("spark.shuffle.io.numConnectionsPerPeer", "3");
    sparkConf.set("spark.shuffle.file.buffer", "128k");
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "20000");
    sparkConf.set("spark.executor.logs.rolling.maxRetainedFiles", "10");
    sparkConf.set("spark.executor.logs.rolling.maxSize", "10m");
    sparkConf.set("spark.executor.logs.rolling.strategy", "size");
    sparkConf.set("spark.shuffle.memoryFraction", "0.4");
    sparkConf.set("spark.storage.memoryFraction", "0.3");
    return sparkConf;
  }




}

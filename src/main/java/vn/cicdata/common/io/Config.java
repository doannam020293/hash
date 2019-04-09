package vn.cicdata.common.io;

import org.apache.spark.SparkConf;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class Config {

    Properties props;


    SparkConf sparkConf;
    String mode;

    public SparkConf getSparkConf() {
        return sparkConf;
    }
    public Config(String filename){
        this.mode = "execution";
        props = getConfig(filename);
        sparkConf = createSparkConfig(this.mode);

    }


    public Config(String filename, String mode){
        this.mode = mode;
        props = getConfig(filename);
        sparkConf = createSparkConfig(this.mode);

    }

    public String getProperty(String input){
        return props.getProperty(input);
    }

    public static Properties getConfig(String filename){
        Properties prop = new Properties();
        try {
            prop.load(Config.class.getClassLoader().getResourceAsStream(filename));

        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return prop;
    }


    public  SparkConf createSparkConfig(String mode) {
        SparkConf sparkConf = new SparkConf();
        sparkConf.set("spark.sql.parquet.binaryAsString", "true");

        switch (mode){
            case "execution":
                sparkConf.set("spark.storage.memoryFraction", "0.1");
                sparkConf.set("spark.memory.fraction", "0.8");
                break;
            case "cache":
                sparkConf.set("spark.storage.memoryFraction", "0.5");
                sparkConf.set("spark.memory.fraction", "0.6");
                break;
        }

        Enumeration<String> en = (Enumeration<String>) props.propertyNames();
        while(en.hasMoreElements()){
            String propName = en.nextElement();
            String propValue = props.getProperty(propName);
            if(propName.startsWith("spark")){
                sparkConf.set(propName, propValue);
            }
        }
        return sparkConf;
    }
    public  SparkConf createSparkConfig() {
        return createSparkConfig("execution");
    }


}

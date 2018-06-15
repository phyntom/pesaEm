package com.pesem.utilities;

import org.jboss.logging.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadConfig {

    private final static Logger Log = Logger.getLogger(LoadConfig.class);

    private String initiatorName;

    private String initiatorPassword;

    private String shortCode;

    private String consumerKey;

    private String consumerSecret;


    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getInitiatorPassword() {
        return initiatorPassword;
    }

    public void setInitiatorPassword(String initiatorPassword) {
        this.initiatorPassword = initiatorPassword;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getConsumerKey() {
        return consumerKey;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }

    /**
     * method used to load Mpesa sandbox account credentials
     */
    public void readMpesaAccountCredentials() {
        try {
            Properties properties = new Properties();
            String configFileName = "config.properties";
            InputStream inputStream = LoadConfig.class.getClassLoader().getResourceAsStream(configFileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file" + configFileName + "not found in the classpath");
            }

            setConsumerKey(properties.getProperty("consumer_key").toString());
            setConsumerSecret(properties.getProperty("consumer_secret").toString());
            setInitiatorName(properties.getProperty("initiator_name").toString());
            setInitiatorPassword(properties.getProperty("initiator_password").toString());
            setShortCode(properties.getProperty("shortcode").toString());

        } catch (IOException ex) {
            Log.error("Cannot load config file ", ex);
        }
    }

    /**
     * method used to read upload file location. user can change this to suit his need before the build the system.
     * the values are taken from the config.properties file
     *
     * @return
     */
    public static String readFilesFolder() {
        try {
            Properties properties = new Properties();
            String configFileName = "config.properties";
            InputStream inputStream = LoadConfig.class.getClassLoader().getResourceAsStream(configFileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file" + configFileName + "not found in the classpath");
            }
            return properties.getProperty("upload_folder");

        } catch (IOException ex) {
            Log.error("Cannot load config file ", ex);
            return "";
        }
    }


}

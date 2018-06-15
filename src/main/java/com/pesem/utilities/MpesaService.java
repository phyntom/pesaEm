package com.pesem.utilities;

import com.pesem.com.pesaem.model.Employee;
import com.pesem.com.pesaem.model.Transaction;
import org.jboss.logging.Logger;

import javax.crypto.Cipher;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Base64.Encoder;

public class MpesaService {

    private static final Logger Log = Logger.getLogger(MpesaService.class.getName());
    private LoadConfig config;
    private Integer expiryTimeSec;

    public MpesaService() {
        config = new LoadConfig();
        config.readMpesaAccountCredentials();
    }

    /**
     * @return
     */
    private String getBasicAuthKey() throws UnsupportedEncodingException {
        config.readMpesaAccountCredentials();
        String consumerKey = config.getConsumerKey();
        String consumerSecret = config.getConsumerSecret();
        String secretKey = consumerKey + ":" + consumerSecret;
        byte[] stringByte = secretKey.getBytes("ISO-8859-1");
        Encoder encoder = Base64.getEncoder();
        String encodedString = encoder.encodeToString(stringByte);
        return encodedString;
    }

    /**
     * @return
     */
    private String getOAuthAccessToken() {
        try {
            String url = "https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
            String authenticationKey = getBasicAuthKey();
            URL apiUrl = new URL(url);

            HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", "Basic " + authenticationKey);

            JsonReader reader = Json.createReader(con.getInputStream());
            JsonObject object = reader.readObject();
            String accessToken = object.getString("access_token");
            expiryTimeSec = Integer.valueOf(object.getString("expires_in"));
            return accessToken;
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
            return "";
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * @param password
     * @return
     * @throws Exception
     */
    private String encrypt(String password) throws Exception {
        InputStream input = MpesaService.class.getClassLoader().getResourceAsStream("cert.cer");
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(input);
        PublicKey pk = certificate.getPublicKey();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pk);
        return Base64.getEncoder().encodeToString(cipher.doFinal(password.getBytes()));
    }

    /**
     * method used to send payments through MPesa-sandbox
     *
     * @param msisdn
     * @param amount
     * @param commandId
     */
    private Transaction sendPayment(String employeeName, String msisdn, BigInteger amount, String commandId) {
        Integer httpResponseCode;
        try {
            Transaction transaction;
            LoadConfig config = new LoadConfig();
            config.readMpesaAccountCredentials();
            String url = "https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest";
            HttpURLConnection con = null;
            StringWriter stringWriter = new StringWriter();
            JsonGenerator generator = Json.createGenerator(stringWriter);
//          String securityCredentials = "iONuXBBPmNlihcFQKk35hBgdxdv+KzLMq1Ozcb68SoXsulChIDWbsr9vGf8QunlPZOaLwi14KRL92ZyNAxNL1OvPALWHy8dPsccszBFUZzZvQszz+k9ZqbGAU4/yOGN/QengmIq7xPIo6F5cc1omlUz8yOx6fyLpHC7eVFx0yn3XpO8IV66ifnUR3Z7pjARUiuJErnlj6e85LbPVM1h6IvnDg2oK3Bu3P749Z8Pmxx9tXB1e6Ge1Awy75oqLeScIRWdE6HDBj5JfTgxZeOtv/CcxxG6zJqX0XwCbLHWxLsyHXne04bansDC9g+K8+puy7GGC4dmz5QRq3snt1J8dXw==";
            String securityCredentials = encrypt("Safaricom864!");
            LocalDateTime transactionTime = LocalDateTime.now();
            generator.writeStartObject()
                    .write("InitiatorName", config.getInitiatorName())
                    .write("SecurityCredential", securityCredentials)
                    .write("CommandID", commandId)
                    .write("Amount", String.valueOf(amount))
                    .write("PartyA", config.getShortCode())
                    .write("PartyB", msisdn)
                    .write("Remarks", "Nothing")
                    .write("QueueTimeOutURL", "http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BConfirmation")
                    .write("ResultURL", "http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BConfirmation")
                    .write("Occasion", "Payment")
                    .writeEnd()
                    .close();
            String jsonData = stringWriter.toString();
            URL apiUrl = new URL(url);
            con = (HttpURLConnection) apiUrl.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", "Bearer " + getOAuthAccessToken());
            con.setRequestProperty("Content-type", "application/json");
            con.connect();
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(jsonData);
            writer.flush();
            String responseDesc = "";
            String conversationID = "";
            String responseData = "";
            String responseCode = "";
            httpResponseCode = con.getResponseCode();
            JsonParser parser;
            if (httpResponseCode > 400) {
                parser = Json.createParser(con.getErrorStream());
            } else {
                parser = Json.createParser(con.getInputStream());
            }
            while (parser.hasNext()) {
                JsonParser.Event e = parser.next();
                if (e == JsonParser.Event.KEY_NAME) {
                    switch (parser.getString()) {
                        case "ResponseCode":
                            parser.next();
                            responseCode = parser.getString();
                            break;
                        case "ResponseDescription":
                            if (parser.next() == JsonParser.Event.VALUE_STRING) {
                                responseDesc = parser.getString();
                            }
                            if (parser.next() == JsonParser.Event.VALUE_NULL) {
                                responseCode = "-1";
                            }
                            break;
                        case "ConversationID":
                            if (parser.next() == JsonParser.Event.VALUE_STRING) {
                                conversationID = parser.getString();
                            }
                            if (parser.next() == JsonParser.Event.VALUE_NULL) {
                                conversationID = "";
                            }
                            break;
                        case "errorCode":
                            parser.next();
                            responseCode = parser.getString();
                            break;
                        case "errorMessage":
                            parser.next();
                            responseDesc = parser.getString();
                            break;
                    }
                }
            }
            transaction = new Transaction(employeeName, msisdn, amount, transactionTime, responseCode, conversationID, responseDesc);
            Log.info("RESPONSE : " + msisdn + "|" + responseCode + "|" + responseDesc + "|" + conversationID);
            return transaction;
        } catch (IOException | NumberFormatException ex) {
            Log.error("Not able to send payment to " + msisdn + " | " + ex.toString());
            return new Transaction();
        } catch (Exception ex) {
            Log.error("Not able to send payment to " + msisdn + " | " + ex.toString());
            return new Transaction();
        }
    }

    /**
     * This method is used to register end point URLs which MPesa Core System will send back the result
     *
     * @param shortCode
     * @param responseType
     * @param confirmationURL
     * @param validationURL
     * @return
     * @throws IOException
     */
    public String registerEndPoint(String shortCode, String responseType, String confirmationURL, String validationURL) throws IOException {

        String url = "https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl";
        HttpURLConnection con = null;
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = Json.createGenerator(stringWriter);
        generator.writeStartObject()
                .write("ShortCode", shortCode)
                .write("ResponseType", responseType)
                .write("ConfirmationURL", confirmationURL)
                .write("ValidationURL", validationURL)
                .writeEnd()
                .close();
        String jsonData = stringWriter.toString();
        System.out.println(jsonData);
        URL apiUrl = new URL(url);
        con = (HttpURLConnection) apiUrl.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        con.setRequestProperty("Authorization", "Bearer " + getOAuthAccessToken());
        con.setRequestProperty("Content-type", "application/json");
        con.setRequestProperty("cache-control", "no-cache");
        con.setConnectTimeout(5000);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(jsonData);
        writer.flush();
        return null;
    }

    /**
     * method used to send payment request and retry if the request fails
     * this is done because sometimes Mpesa sandbox refusing to process request at the first time
     * saying its is in maintenance windows
     *
     * @param employee
     * @return
     */
    public Transaction processRequest(Employee employee) {
        Transaction trans = sendPayment(employee.getName(), employee.getPhoneNumber(), employee.getAmount(), "BusinessPayment");
        String responseCode=trans.getResponseCode();
        if(responseCode.equals("500.002.1001")){
            int countTry = 0;
            do {
                trans = sendPayment(employee.getName(), employee.getPhoneNumber(), employee.getAmount(), "BusinessPayment");
                if (responseCode.equals("0")) {
                    break;
                }
                countTry++;
            } while (countTry < 1);
        }
        return trans;
    }


}

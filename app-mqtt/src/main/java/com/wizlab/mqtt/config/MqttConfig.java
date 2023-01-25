package com.wizlab.mqtt.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.net.ssl.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.BlockingQueue;

@Configuration
public class MqttConfig {

    // private static final String USERNAME = "userB";
    // private static final String PASSWORD = "pass";
    private static final String CLIENT_PUB_ID = MqttClient.generateClientId();
    private static final String CLIENT_SUB_ID = MqttClient.generateClientId();
    private static final String TOPIC_FILTER = "hayoung_things/test";
    private static final String secureURL = "ssl://211.197.19.242:48883";
    static ClassPathResource caResource = new ClassPathResource("tls/ca.crt");
    static ClassPathResource clientCrtResource = new ClassPathResource("tls/client.crt");
    static ClassPathResource clientKeyResource = new ClassPathResource("tls/client.key");

    private BlockingQueue<String> mqttSubQueue;

    /** Connect Option */
    private MqttConnectOptions connectOptions() throws Exception {
        MqttConnectOptions options = new MqttConnectOptions();

        try {
            String caFilePath = caResource.getFile().getAbsolutePath();
            String clientCrtFilePath = clientCrtResource.getFile().getAbsolutePath();
            String clientKeyFilePath = clientKeyResource.getFile().getAbsolutePath();
            SSLSocketFactory socketFactory = getSocketFactory(caFilePath,
                    clientCrtFilePath, clientKeyFilePath, "");
            options.setSocketFactory(socketFactory);
            options.setHttpsHostnameVerificationEnabled(false);
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);
            options.setServerURIs(new String[]{secureURL});
            options.setMaxInflight(100);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return options;
    }

    private static SSLSocketFactory getSocketFactory(final String caCrtFile,
                                                    final String crtFile,
                                                    final String keyFile,
                                                    final String password) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        // load CA certificate
        X509Certificate caCert = null;

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(caCrtFile));
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        while (bis.available() > 0)
        {
            caCert = (X509Certificate) cf.generateCertificate(bis);
        }

        // load client certificate
        bis = new BufferedInputStream(new FileInputStream(crtFile));
        X509Certificate cert = null;
        while (bis.available() > 0) {
            cert = (X509Certificate) cf.generateCertificate(bis);
        }

        // load client private key
        PEMParser pemParser = new PEMParser(new FileReader(keyFile));
        Object object = pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
        KeyPair key = converter.getKeyPair((PEMKeyPair) object);
        pemParser.close();

        // CA certificate is used to authenticate server
        KeyStore caKs = KeyStore.getInstance(KeyStore.getDefaultType());
        caKs.load(null, null);
        caKs.setCertificateEntry("ca-certificate", caCert);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(caKs);

        // client key and certificates are sent to server, so it can authenticate
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);
        ks.setCertificateEntry("certificate", cert);
        ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(),
                new java.security.cert.Certificate[] { cert });
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, password.toCharArray());

        // finally, create SSL socket factory
        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        return context.getSocketFactory();
    }

    /** 브로커 메시지 Publish Client Factory */
    @Bean
    public DefaultMqttPahoClientFactory defaultMqttPahoClientFactory() throws Exception {
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(connectOptions());
        return clientFactory;
    }

    /** 브로커 메시지 Consumer Channel */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /** 브로커 메시지 Consumer Adapter */
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter;

        try {
//            adapter = new MqttPahoMessageDrivenChannelAdapter(secureURL, CLIENT_SUB_ID, TOPIC_FILTER);
            adapter = new MqttPahoMessageDrivenChannelAdapter(CLIENT_SUB_ID, defaultMqttPahoClientFactory(), TOPIC_FILTER);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            String msg = message.getPayload().toString();

            pushQueue(topic, msg);
        };
    }

    public void attachMqttSubQueue(BlockingQueue<String> mqttSubQueue) {
        if(this.mqttSubQueue == null) {
            this.mqttSubQueue = mqttSubQueue;
        }
    }

    public void pushQueue(String topic, String msg) {
        // log.info("pushQueue - TOPIC -> {} || MSG -> {}", topic, msg);
        String convertMessage = String.format("%s^%s", topic, msg);
        mqttSubQueue.add(convertMessage);
    }

    /** 브로커 메시지 Publish Channel */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() throws Exception {
        MqttPahoMessageHandler messageHandler =
                    new MqttPahoMessageHandler(secureURL, CLIENT_PUB_ID, defaultMqttPahoClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultRetained(false);
        messageHandler.setDefaultQos(1);
        messageHandler.setDefaultTopic(TOPIC_FILTER);
        return messageHandler;
    }
}

package com.xiaoaiframework.spring.activemq;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author edison
 */
public class DataSource {

    /**
     * Whether the default destination type is topic.
     */
    private boolean pubSubDomain = true;

    private boolean primary = false;

    private String id;

    private String url;

    private String port;

    private String username;

    private String password;


    public URI getBrokerURl(){

        try {
            if(url.matches("^tcp://(.*)")){
                return new URI(this.url);
            }
            return new URI("tcp://"+this.url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public boolean isPubSubDomain() {
        return pubSubDomain;
    }

    public boolean isPrimary() {
        return primary;
    }


}

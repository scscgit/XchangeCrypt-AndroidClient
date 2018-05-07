package cloud.coders.sk.xchangecrypt.datamodel;

import io.swagger.client.model.Account;

/**
 * Created by Peter on 28.04.2018.
 */

public class User extends BaseObject {

    private String AccountId;

    public User(String accountId) {
        AccountId = accountId;
    }

    public User(int id, String accountId) {
        super(id);
        AccountId = accountId;
    }

    private String name = "Peter Čerešník";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountId() {
        return AccountId;
    }


    private String idToken;
    private String accessToken;
    private String refreshToken;

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private String login;

    private String password;

 //   private String accessToken;

    private double expiration;

//    public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }

    public double getExpiration() {
        return expiration;
    }

    public void setExpiration(double expiration) {
        this.expiration = expiration;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

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

    public String getAccountId() {
        return AccountId;
    }

    private String login;

    private String password;

    private String accessToken;

    private double expiration;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

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

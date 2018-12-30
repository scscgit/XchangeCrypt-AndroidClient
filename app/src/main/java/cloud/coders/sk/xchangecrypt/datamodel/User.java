package cloud.coders.sk.xchangecrypt.datamodel;

/**
 * Created by Peter on 28.04.2018.
 */
public class User {
    // User data
    private String userId;
    private String accountId;
    private String login;
    private String emailAddress;
    private String realName;
    @Deprecated
    private String password;

    // Authentication tokens
    private String idToken;
    private String accessToken;
    private String refreshToken;
    private double expiration;

    public User(String userId, String login, String emailAddress, String realName) {
        this.userId = userId;
        this.login = login;
        this.emailAddress = emailAddress;
        this.realName = realName;
        this.accountId = "0";
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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

    public double getExpiration() {
        return expiration;
    }

    public void setExpiration(double expiration) {
        this.expiration = expiration;
    }
}

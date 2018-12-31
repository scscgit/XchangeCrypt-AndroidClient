package io.swagger.client.auth;

/**
 * Created by Peter on 26.04.2018.
 */
public class FastAccessTokenApiKey extends ApiKeyAuth {
  public FastAccessTokenApiKey() {
    super("header", "authorization");
  }

  @Override
  public void setApiKey(String apiKey) {
    super.setApiKey("Bearer " + apiKey);
  }
}

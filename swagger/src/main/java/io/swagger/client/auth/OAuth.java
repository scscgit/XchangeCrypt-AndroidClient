package io.swagger.client.auth;

import io.swagger.client.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by Peter on 26.04.2018.
 */
public class OAuth implements Authentication {
  @Override
  public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
  }
}

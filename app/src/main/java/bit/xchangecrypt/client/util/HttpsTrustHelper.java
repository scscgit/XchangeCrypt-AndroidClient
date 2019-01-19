package bit.xchangecrypt.client.util;

import android.util.Log;
import bit.xchangecrypt.client.BuildConfig;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/**
 * Do not use in production!
 */
public class HttpsTrustHelper {
    public static void allowAllSSL() {
        Log.wtf(HttpsTrustHelper.class.getSimpleName(), "Disabling HTTPS certificate verification for dev purposes!");
        if (!BuildConfig.DEBUG) {
            throw new RuntimeException("Do not use in production!");
        }
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("TLS");
            context.init(
                null,
                new TrustManager[]{
                    new X509TrustManager() {
                        private final X509Certificate[] acceptedIssuers = new X509Certificate[]{};

                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] x509Certificates, String s) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return acceptedIssuers;
                        }
                    }
                },
                new SecureRandom()
            );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }
}

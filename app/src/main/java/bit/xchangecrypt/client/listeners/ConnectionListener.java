package bit.xchangecrypt.client.listeners;

/**
 * Created by jakubcervenak on 07/12/17.
 */
public interface ConnectionListener {
    void onReconnection();

    void onConnectionLost();
}
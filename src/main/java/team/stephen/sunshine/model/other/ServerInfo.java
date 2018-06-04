package team.stephen.sunshine.model.other;

/**
 * @author Stephen
 * @date 2018/06/04 17:18
 */
public class ServerInfo {
    private String host;
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public ServerInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }
}

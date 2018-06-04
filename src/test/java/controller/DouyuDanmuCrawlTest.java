package controller;


import com.github.pagehelper.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.model.other.*;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.util.common.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class DouyuDanmuCrawlTest {
    private static final String REGEX_ROOM_ID = "\"room_id\":(\\d*),";
    private static final String REGEX_ROOM_STATUS = "\"show_status\":(\\d*),";
    private static final String REGEX_SERVER = "%7B%22ip%22%3A%22(.*?)%22%2C%22port%22%3A%22(.*?)%22%7D%2C";
    private static final String REGEX_GROUP_ID = "type@=setmsggroup.*/rid@=(\\d*?)/gid@=(\\d*?)/";
    private static final String REGEX_DANMAKU_SERVER = "/ip@=(.*?)/port@=(\\d*?)/";
    private static final String REGEX_CHAT_DANMAKU = "type@=chatmsg/.*rid@=(\\d*?)/.*uid@=(\\d*).*nn@=(.*?)/txt@=(.*?)/(.*)/";

    @Test
    public void testDanmu() {
        String uril = "https://www.douyu.com/ym1314";
        try {
            String html = HttpUtils.okrHttpGet(uril);
            int rid = parseRoomId(html);
            boolean online = parseOnline(html);
            if (!online) {
                LogRecord.print("该房间还没有直播！" + uril);
                return;
            }
            LogRecord.print("获取服务器列表 ...");
            List<ServerInfo> serverList = parseServerInfo(html);

            if (serverList == null || serverList.size() <= 0) {
                LogRecord.print("获取服务器列表失败！");
                return;
            }
            Socket socket = null;

            ServerInfo server = serverList.get(0);

            try {
                LogRecord.print("连接服务器 " + server.getHost() + ":" + server.getPort());
                socket = new Socket(server.getHost(), server.getPort());

                String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
                String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();
                String vk = SecurityUtil.md5(timestamp + "7oE9nPEG9xXV69phU31FYCLUagKeYtsF" + uuid);
                //发送登陆请求
                String content = gid(rid, uuid, timestamp, vk);

                if (socket == null || !socket.isConnected()) return;

                Message message = new Message(content);
                OutputStream out = socket.getOutputStream();
                out.write(message.getBytes());

                LogRecord.print("Send Message " + message.toString());
                if (socket == null || !socket.isConnected()) return;

                int len;
                byte[] buffer = new byte[8 * 1024];
                InputStream in = socket.getInputStream();

                while (socket.isConnected() //链接结束
                        && (len = in.read(buffer)) != -1 //输入流结束
                        ) {
                    if (buffer == null || buffer.length <= 0) return;

                    List<String> resList = new ArrayList<>();
                    String byteArray = HexUtil.bytes2HexString(buffer).toLowerCase();

                    String[] responseStrings = byteArray.split("b2020000");
                    int end;
                    for (int i = 1; i < responseStrings.length; i++) {
                        if (!responseStrings[i].contains("00")) continue;
                        end = responseStrings[i].indexOf("00");
                        byte[] bytes = HexUtil.hexString2Bytes(responseStrings[i].substring(0, end));
                        if (bytes != null) resList.add(new String(bytes));
                    }
                    LogRecord.print(resList);

                    boolean f1 = false, f2 = false;

                    for (String response : resList) {
                        LogRecord.print("Receive Response" + response);
                        if (response.contains("msgrepeaterlist")) {
                            //获取弹幕服务器地址
                            LogRecord.print("获取弹幕服务器地址 ...");
                            String danmakuServerStr = SttCode.deFilterStr(SttCode.deFilterStr(response));
                            List<ServerInfo> danmakuServers = parseDanmakuServer(danmakuServerStr);
                            LogRecord.print("获取到 " + danmakuServers.size() + " 个服务器地址 ...");
                            f1 = true;
                        }

                        if (response.contains("setmsggroup")) {
                            //获取gid
                            LogRecord.print("获取弹幕群组ID ...");
                            int gid = parseID(response);
                            LogRecord.print("弹幕群组ID：" + gid);
                            f2 = true;
                        }
                    }

//                    if (listener != null) {
//                        listener.onReceive(splitResponse(Arrays.copyOf(buffer, len)));
//                        if (listener.isFinished()) return;
//                    }
                }

                //等待接收
//                MessageHandler.receive(socket, loginListener);

            } catch (IOException e) {
                LogRecord.print("Error" + e.toString());
                LogRecord.print("登陆到服务器失败！");
            } finally {
                if (socket != null)
                    try {
                        socket.close();
                    } catch (IOException e) {
                        LogRecord.print("Error" + e.toString());
                        LogRecord.print("连接关闭异常！");
                    }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String gid(int roomId, String devid, String rt, String vk) {
        return String.format("type@=loginreq/username@=/ct@=0/password@=/roomid@=%d/devid@=%s/rt@=%s/vk@=%s/ver@=20150929/", roomId, devid, rt, vk);
    }

    public static List<ServerInfo> parseDanmakuServer(String content) {
        if (content == null) return null;

        Matcher matcher = getMatcher(content, REGEX_DANMAKU_SERVER);
        List<ServerInfo> serverList = new ArrayList<>();

        while (matcher.find()) {
            ServerInfo serverInfo = new ServerInfo(matcher.group(1), Integer.parseInt(matcher.group(2)));
            serverList.add(serverInfo);

            LogRecord.print("Parse DanmakuServer" + serverInfo.toString());
        }
        return serverList;
    }

    public static int parseRoomId(String content) {
        int rid = -1;
        if (content == null) return rid;

        Matcher matcher = getMatcher(content, REGEX_ROOM_ID);
        if (matcher.find()) {
            rid = Integer.parseInt(matcher.group(1));
        }

        LogRecord.print("Parse RoomId:" + rid + "");
        return rid;
    }

    public static boolean parseOnline(String content) {
        if (content == null) return false;

        Matcher matcher = getMatcher(content, REGEX_ROOM_STATUS);
        return matcher.find() && "1".equals(matcher.group(1));
    }

    /**
     * 解析  gid
     */
    public static int parseID(String response) {
        if (response == null) return -1;

        Matcher matcher = getMatcher(response, REGEX_GROUP_ID);
        int gid = -1;
        if (matcher.find()) {
            gid = Integer.parseInt(matcher.group(2));
        }

        LogRecord.print("Parse ID GId -> " + gid);

        return gid;
    }

    public static List<ServerInfo> parseServerInfo(String content) {
        if (content == null) return null;

        Matcher matcher = getMatcher(content, REGEX_SERVER);
        List<ServerInfo> serverList = new ArrayList<>();

        while (matcher.find()) {
            ServerInfo serverInfo = new ServerInfo(matcher.group(1), Integer.parseInt(matcher.group(2)));
            serverList.add(serverInfo);

            LogRecord.print("Parse ServerInfo:" + serverInfo.getHost() + "\t" + serverInfo.getPort());
        }
        return serverList;
    }

    private static Matcher getMatcher(String content, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        return pattern.matcher(content);
    }
}

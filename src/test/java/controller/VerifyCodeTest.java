package controller;


import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import com.sun.org.apache.bcel.internal.generic.LOR;
import net.sourceforge.tess4j.util.ImageHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.Application;
import team.stephen.sunshine.service.other.WeiboService;
import team.stephen.sunshine.util.SvmPredict;
import team.stephen.sunshine.util.SvmTrain;
import team.stephen.sunshine.util.bean.WeiboVerifyResult;
import team.stephen.sunshine.util.common.HttpUtils;
import team.stephen.sunshine.util.common.LogRecord;
import team.stephen.sunshine.util.element.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class VerifyCodeTest {
//        @Autowired
//    private WeiboService weiboService;
    private String baseVerifyCodePath = "C:\\Users\\Stephen\\Desktop\\sunshine\\weibo\\code";
    private String baseTrainPath = "C:\\Users\\Stephen\\Desktop\\sunshine\\weibo\\code\\train";
    private int start = 400;
    private int end = 500;
    private static final int Y_SCALE = 1000;
    private static final String svmBaseDir = "C:\\Users\\Stephen\\Desktop\\sunshine\\weibo\\code\\svm\\";
    private static final String svmTrainFilePath = svmBaseDir + "train\\train.txt";
    private static final String svmModelFilePath = svmBaseDir + "model\\model.txt";
    private static final String svmTestFilePath = svmBaseDir + "test\\test-%s.txt";
    private static final String svmToTestFilePath = svmBaseDir + "totest";
    private static final String svmPredictFilePath = svmBaseDir + "predict\\predict-%s.txt";

    private static String cookie =
            "_s_tentry=movie.kankan.com; Apache=7256170564393.524.1525878160642; SINAGLOBAL=7256170564393.524.1525878160642; ULV=1525878160649:1:1:1:7256170564393.524.1525878160642:; login_sid_t=fe72f64d0dbba7f7b0e63c5616fa15d4; cross_origin_proto=SSL; SWBSSL=usrmdinst_7; SWB=usrmdinst_15; UOR=,,graph.qq.com; crossidccode=CODE-yf-1FqE6a-Fy3Nn-Ajo30mzwICz3m1d561a92; ALF=1559849282; SSOLoginState=1528313282; SCF=ApWJpYkIBCSLvQa6VugVvlZ6e-DWM2_b7Y4Eih38-j3oLHovnRTfnSFHy3SpiZywB53HkqhV_o1jefZpb9HPxDI.; SUB=_2A252HEWTDeRhGeNJ7FYT8SnIyDiIHXVVaDBbrDV8PUNbmtANLVrbkW9NS7N__l4RArQ-48Qwz27OI77ToT5jjwxo; SUBP=0033WrSXqPxfM725Ws9jqgMF55529P9D9WhafKh-AK_DR8KR-fW3sBuy5JpX5KzhUgL.Fo-NS0BEeKMXe0B2dJLoIE5LxK-LB-BL1-qLxK-L1hMLBK2LxKnLBo-L1-zN1hM7S5tt;  WBStorage=5548c0baa42e6f3d|undefined; ULOGIN_IMG=15283356858116;SUHB=0C1SY15AkiXDpl; wvr=6";
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Cookie", cookie);
    }

    @Test
    public  void testCookieTruncted(){
        LogRecord.print(cookie.replaceAll("ULOGIN_IMG=(\\d{1,})","ULOGIN_IMG=new ulogin"));
    }

    @Test
    public void testPostVerify() {

        String verifyUrl = "http://s.weibo.com/ajax/pincode/pin";
//        WeiboVerifyResult vr = weiboService.getVerifyCodeResult(verifyUrl);
//        LogRecord.print(weiboService.verifyCode(headers,cookie));
//        LogRecord.print(vr.getCode()+"\t"+vr.getuLoginImg());
//        cookie=cookie.substring(0,cookie.indexOf("ULOGIN_IMG")+11);
//        cookie=cookie+vr.getuLoginImg();
//        LogRecord.print(cookie);
//        headers.put("Cookie",cookie);
//        String url = "https://s.weibo.com/ajax/pincode/verified";
////        url="http://s.weibo.com/ajax/pincode/verified";
////        testUrlPost(url);
////        if(true){
////            return;
////        }
//        Map<String, String> body = new HashMap<>();
//        body.put("secode", vr.getCode());
//        body.put("type", "sass");
//        body.put("pageid", "weibo");
//        body.put("_t", "0");
////        headers.put("Host", "s.weibo.com");
////        headers.put("Origin", "https://s.weibo.com");
//        headers.put("Referer","https://s.weibo.com/weibo/%25E5%258D&Refer=index");
//        headers.put("Cookie",cookie);
//        headers.put("X-Requested-With", "XMLHttpRequest");
//        try {
//            String res = HttpUtils.okrHttpPost(url, headers, body);
////            String res = IOUtils.toString(HttpUtils.httpPost(url, headers, body).getEntity().getContent());
//            LogRecord.print(res);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private void testUrlPost(String uri) {
        String result = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false); // post方式不能使用缓存
            con.setRequestProperty("Cookie", cookie);
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 请求正文信息
            // 第一部分：
            StringBuilder sb = new StringBuilder();
            sb.append("--"); // 必须多两道线
            // 这里说明下，这两个横杠是http协议要求的，用来分隔提交的参数用的，不懂的可以看看http
            // 协议头
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"secode\" \r\n\r\n"); // 这里是参数名，参数名和值之间要用两次
            sb.append("ssss" + "\r\n"); // 参数的值

            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"type\" \r\n\r\n");
            sb.append("sass" + "\r\n");

            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"pageid\" \r\n\r\n");
            sb.append("weibo" + "\r\n");
            sb.append("--"); // 必须多两道线
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"_t\" \r\n\r\n");
            sb.append(0 + "\r\n");

            byte[] head = sb.toString().getBytes("utf-8");
            // 获得输出流
            OutputStream out = new DataOutputStream(con.getOutputStream());
            // 输出表头
            out.write(head);
            // 结尾部分，这里结尾表示整体的参数的结尾，结尾要用"--"作为结束，这些都是http协议的规定
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
            out.write(foot);
            out.flush();
            out.close();
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = null;
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(
                    con.getInputStream(), "utf-8"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        LogRecord.print(result);
    }

    @Test
    public void testRecognize() {
        String url = "https://s.weibo.com/ajax/pincode/pin?type=sass152818473708100";
//        LogRecord.print(weiboService.getVerifyCodeResult(url));
        if (true) {
            return;
        }
        OkHttpClient httpClient = new OkHttpClient();
        Request.Builder builder = new Request.Builder().url(url);
        Request request = builder.build();
        InputStream in;
        String fileName = baseVerifyCodePath + "/raw/test.png";
        try {
            in = httpClient.newCall(request).execute().body().byteStream();
            byte[] tempbytes = new byte[100];
            int byteread;
            OutputStream outputStream = new FileOutputStream(new File(fileName));
            while ((byteread = in.read(tempbytes)) != -1) {
                outputStream.write(tempbytes, 0, byteread);
            }
            outputStream.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        BufferedImage grayImage = null;
        try {
            grayImage = ImageHelper.convertImageToBinary(ImageIO.read(new File(fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        removeZaoyin(grayImage);

        BufferedWriter writer = null;
        try {
            writer = Files.newWriter(new File(svmTestFilePath), Charsets.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        try {
            List<BufferedImage> subs = getBufferedImages(grayImage);
            for (BufferedImage sub : subs) {
                StringBuilder sb = getTrainData(sub, "0");
                writer.write(sb.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String testFile = svmTestFilePath;
        String predictPath = svmPredictFilePath;
        try {
            testSvmVerify(testFile, predictPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        testGenSvmResult(predictPath);
    }

    @Test
    public void downloadVerifyCode() {
        String url = "https://s.weibo.com/ajax/pincode/pin?type=sass152818473708100";
        for (int i = start; i < end; i++) {

            OkHttpClient httpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder().url(url);
            Request request = builder.build();
            try {
                byte[] tempbytes = new byte[100];
                int byteread = 0;
                InputStream in = httpClient.newCall(request).execute().body().byteStream();
                OutputStream outputStream = new FileOutputStream(new File(baseVerifyCodePath + "/raw/" + i + ".png"));
                while ((byteread = in.read(tempbytes)) != -1) {
                    outputStream.write(tempbytes, 0, byteread);
                }
                outputStream.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testChange() throws IOException {
        for (int i = start; i < end; i++) {
            String filePath = baseVerifyCodePath + "/raw/" + i + ".png";
            BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(new File(filePath)));
            removeZaoyin(grayImage);
            try {
                ImageIO.write(grayImage, "tif", new File(baseVerifyCodePath + "/binary/" + i + ".tif"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void removeZaoyin(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (isWhite(img.getRGB(x, y))) {
                    img.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    if (isZaoyin(img, x, y)) {
                        img.setRGB(x, y, Color.WHITE.getRGB());
                    } else {
                        img.setRGB(x, y, Color.BLACK.getRGB());
                    }
                }

            }
        }
    }

    @Test
    public void testSubImag() throws IOException {
        for (int i = start; i < end; i++) {
            try {
                String filePath = baseVerifyCodePath + "/binary-pro/" + i + ".tif";
                java.util.List<BufferedImage> subImages = getSubImages(filePath);

                for (int index = 0; index < subImages.size(); index++) {
                    File file = new File(baseVerifyCodePath + "/train/第" + index + "-" + i + ".tif");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    ImageIO.write(subImages.get(index), "tif", file);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testGenSvmTrainData() throws IOException {
        try {
            BufferedWriter writer = Files.newWriter(new File(svmTrainFilePath), Charsets.UTF_8);

            String filePath = baseVerifyCodePath + "/train";
            for (File file : new File(filePath).listFiles()) {
                BufferedImage image = ImageIO.read(file);
                StringBuilder sb = getTrainData(image, file.getName());
                writer.write(sb.toString());
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StringBuilder getTrainData(BufferedImage img, String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(getInt(filename)).append(" ");
        sb.append(getSvmDataFromImage(img));
        sb.append("\r\n");
        return sb;
    }

    private StringBuilder getSvmDataFromImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                sb.append(y * Y_SCALE + x).append(":").append(getWeight(img, x, y)).append(" ");
            }
        }
        return sb;
    }

    private int getInt(String s) {
        String f = s.substring(0, 1);
        try {
            return Integer.parseInt(f);
        } catch (Exception e) {
        }
        char c = f.charAt(0);
        return (c - 'a') + 10;
    }

    private double getWeight(BufferedImage img, int x, int y) {
        if (isWhite(img.getRGB(x, y))) {
            return 0.01;
        }
        return 0.99;
    }

    @Test
    public void testSvmTrain() throws IOException {
        String[] arg = getTrainModelPath();
        SvmTrain.main(arg);
    }

    private static String[] getTrainModelPath() {
        return new String[]{
                "-g", "2.0", "-c", "32", "-t", "0", "-m", "500.0", "-h", "0", "-b", "1",
                //训练集
                svmTrainFilePath,
                svmModelFilePath};
    }

    @Test
    public void testGenSvmTestData() throws IOException {
        try {

            for (File file : new File(svmToTestFilePath).listFiles()) {
                BufferedWriter writer = Files.newWriter(new File(String.format(svmTestFilePath, file.getName().substring(0, file.getName().indexOf(".")))), Charsets.UTF_8);
                BufferedImage img = ImageIO.read(file);
                List<BufferedImage> subs = getBufferedImages(img);
                for (BufferedImage sub : subs) {
                    StringBuilder sb = getTrainData(sub, file.getName());
                    writer.write(sb.toString());
                }
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //2,6,s
    @Test
    public void testGenSvmVerify() throws IOException {
        for (int i = start; i < end; i++) {
            String testFile = String.format(svmTestFilePath, i + "");
            String predictPath = String.format(svmPredictFilePath, i + "");
            testSvmVerify(testFile, predictPath);
            LogRecord.print(i);
            testGenSvmResult(predictPath);
        }
    }

    public void testSvmVerify(String testFile, String predictPath) throws IOException {
        String[] parg = {testFile, //测试数据
                svmModelFilePath, // 调用训练模型
                predictPath,
                "-g", "2.0", "-c", "100", "-t", "0", "-m", "500.0", "-h", "0",
                "-b", "1", "-v", "5"}; //预测结果
        try {
            SvmPredict.main(parg);  //调用
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVerify() throws IOException {
        for (int i = start; i < end; i++) {
            List<String> codes = new ArrayList<>();
            try {
                String filePath = baseVerifyCodePath + "/binary-pro/" + i + ".tif";
                List<BufferedImage> subImages;
                try {
                    subImages = getSubImages(filePath);
                } catch (Exception e) {
                    LogRecord.print("error");
                    continue;
                }
                File dir = new File(baseTrainPath);
                File[] trianFiles = dir.listFiles();
                for (BufferedImage subImage : subImages) {
                    String code = null;
                    int max = 0;
                    for (File trianFile : trianFiles) {
                        int score = computeScore(trianFile, subImage);
                        if (score > max) {
                            max = score;
                            code = trianFile.getName();
                        }
                    }
                    codes.add(code.substring(0, 1));
                }
            } catch (Exception e) {
//                e.printStackTrace();
            }
            LogRecord.print(i + ":\t" + Joiner.on(" ").join(codes));
            testGenSvmResult(String.format(svmPredictFilePath, i));
        }
    }

    public List<String> testGenSvmResult(String filePath) {
        List<String> result;
        List<String> codes = new ArrayList<>();
        try {
            result = Files.readLines(new File(filePath), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return codes;
        }
        try {
            for (int j = 1; j < result.size(); j++) {
                String item = result.get(j);
                if (StringUtils.isNotNull(item)) {
                    codes.add(getChar(Integer.parseInt(item.substring(0, item.indexOf(".")))));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogRecord.print(Joiner.on(" ").join(codes));
        return codes;
    }

    private String getChar(int i) {
        if (i < 10) {
            return i + "";
        }
        return String.valueOf((char) ('a' + (i - 10)));

    }

    private int computeScore(File trianFile, BufferedImage subImage) {
        int count = 0;
        try {
            BufferedImage trainImg = ImageIO.read(trianFile);
            for (int x = 0; x < subImage.getWidth(); x++) {
                for (int y = 0; y < subImage.getHeight(); y++) {
                    if (x < trainImg.getWidth() && y < trainImg.getHeight()) {
                        if (!isWhite(subImage.getRGB(x, y)) && !isWhite(trainImg.getRGB(x, y))) {
                            count++;
                        }
//                        if (isWhite(subImage.getRGB(x, y)) && isWhite(trainImg.getRGB(x, y))) {
//                            count++;
//                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    private List<BufferedImage> getSubImages(String filePath) throws IOException {
        BufferedImage img = ImageIO.read(new File(filePath));
        return getBufferedImages(img);
    }

    private List<BufferedImage> getBufferedImages(BufferedImage img) {
        List<BufferedImage> subImages = new ArrayList<>();
        int width = img.getWidth();

        boolean has = false;
        int startX = 0;
        int endX = 0;
        for (int x = 0; x < width; ++x) {
            try {
                if (isEmpty(img, x, true)) {
                    if (has) {
                        endX = x - 1;
                        BufferedImage sub = img.getSubimage(startX, 0, endX - startX, img.getHeight());
                        int startY = 0;
                        int endY = 0;
                        for (int y = 0; y < sub.getHeight(); y++) {
                            if (!isEmpty(sub, y, false)) {
                                startY = y;
                                break;
                            }
                        }
                        for (int y = sub.getHeight() - 1; y >= 0; y--) {
                            if (!isEmpty(sub, y, false)) {
                                endY = y;
                                break;
                            }
                        }
                        try {
                            sub = img.getSubimage(startX, startY, endX - startX, endY - startY);
                            subImages.add(sub);
                        } catch (Exception e) {
//                        e.printStackTrace();
                        }
                        has = false;
                    }
                    continue;
                }
                if (!has) {
                    startX = x;
                    has = true;
                }
            } catch (Exception e) {

            }
        }
        return subImages;
    }

    private boolean isEmpty(BufferedImage image, int x, boolean column) {
        if (column) {
            for (int k = 0; k < image.getHeight(); k++) {
                if (!isWhite(image.getRGB(x, k))) {
                    return false;
                }
            }
        } else {
            for (int k = 0; k < image.getWidth(); k++) {
                if (!isWhite(image.getRGB(k, x))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isZaoyin(BufferedImage img, int x, int y) {
        if (x == 0 || y == 0 || x == img.getWidth() - 1 || y == img.getHeight() - 1 || (isWhite(img.getRGB(x - 1, y - 1)) &&
                isWhite(img.getRGB(x - 1, y)) &&
                isWhite(img.getRGB(x - 1, y + 1)) &&
                isWhite(img.getRGB(x, y - 1)) &&
                isWhite(img.getRGB(x, y + 1)) &&
                isWhite(img.getRGB(x + 1, y - 1)) &&
                isWhite(img.getRGB(x + 1, y)) &&
                isWhite(img.getRGB(x + 1, y + 1)))) {
            return true;
        }
        return false;
    }

    public static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
            return true;
        }
        return false;
    }
}

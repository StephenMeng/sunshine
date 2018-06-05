package controller;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.util.common.LogRecord;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class VerifyCodeTest {
    private String baseVerifyCodePath = "C:\\Users\\DELL\\Desktop\\sunshine\\weibo\\code";

    @Test
    public void downloadVerifyCode() {
        String url = "https://s.weibo.com/ajax/pincode/pin?type=sass152818473708100";
        for (int i = 1; i < 100; i++) {

            OkHttpClient httpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder().url(url);
            Request request = builder.build();
            try {
                byte[] tempbytes = new byte[100];
                int byteread = 0;
                InputStream in = httpClient.newCall(request).execute().body().byteStream();
                OutputStream outputStream = new FileOutputStream(new File(baseVerifyCodePath + "/" + i + ".png"));
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
    public void testVerifyCode() throws IOException {
        for (int i = 1; i < 10; i++) {
            String filePath = baseVerifyCodePath + "/" + i + ".png";
            File imageFile = new File(filePath);
            BufferedImage image = ImageIO.read(new FileInputStream(imageFile));
            BufferedImage textImage = ImageHelper.convertImageToGrayscale(image);
// 图片锐化,自己使用中影响识别率的主要因素是针式打印机字迹不连贯,所以锐化反而降低识别率
// textImage = ImageHelper.convertImageToBinary(textImage);
// 图片放大5倍,增强识别率(很多图片本身无法识别,放大5倍时就可以轻易识,但是考滤到客户电脑配置低,针式打印机打印不连贯的问题,这里就放大5倍)
            textImage = ImageHelper.getScaledInstance(textImage, textImage.getWidth() * 5, textImage.getHeight() * 5);
            Tesseract instance = new Tesseract();
            instance.setDatapath(baseVerifyCodePath);
            //将验证码图片的内容识别为字符串
            try {
                String result = instance.doOCR(textImage);
                LogRecord.print(result);
            } catch (TesseractException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testChange() {
    }
}

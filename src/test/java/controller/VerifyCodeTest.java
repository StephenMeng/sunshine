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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

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
            String filePath = baseVerifyCodePath + "/binary-pro/" + i + ".tif";
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
    public void testChange() throws IOException {
        for (int i = 1; i < 100; i++) {
            String filePath = baseVerifyCodePath + "/raw/" + i + ".png";
            BufferedImage grayImage = ImageHelper.convertImageToBinary(ImageIO.read(new File(filePath)));
            try {
                ImageIO.write(grayImage, "tif", new File(baseVerifyCodePath + "/binary/" + i + ".tif"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testRemoveZaoyin() throws IOException {
        for (int i = 1; i < 100; i++) {
            String filePath = baseVerifyCodePath + "/binary/" + i + ".tif";
            BufferedImage img = ImageIO.read(new File(filePath));
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
            ImageIO.write(img, "tif", new File(baseVerifyCodePath + "/binary-pro/" + i + ".tif"));

        }

    }

    @Test
    public void testSubImag() throws IOException {
        for (int i = 1; i < 100; i++) {
            try {
                java.util.List<BufferedImage> subImages = new ArrayList<>();
                String filePath = baseVerifyCodePath + "/binary-pro/" + i + ".tif";
                BufferedImage img = ImageIO.read(new File(filePath));
                int width = img.getWidth();
                int height = img.getHeight();

                boolean has = false;
                int startX = 0;
                int endX = 0;
                for (int x = 0; x < width; ++x) {
                    if (isEmpty(img, x, true)) {
                        if (has) {
                            endX = x - 1;
                            LogRecord.print(startX + "\t" + endX);
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
                            LogRecord.print(startY + "\t" + endY);
                            try {
                                sub = img.getSubimage(startX, startY, endX - startX, endY - startY);
                                subImages.add(sub);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            has = false;
                        }
                        continue;
                    }
                    if (!has) {
                        startX = x;
                        has = true;
                    }
                }

                for (int index = 0; index < subImages.size(); index++) {
                    ImageIO.write(subImages.get(index), "tif", new File(baseVerifyCodePath + "/train/" + index + "-" + i + ".tif"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

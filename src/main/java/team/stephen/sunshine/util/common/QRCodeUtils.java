package team.stephen.sunshine.util.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 *
 * @author stephen
 * @date 2018/5/22
 */
public class QRCodeUtils {
    //二维码颜色
    private static final int BLACK = 0xFF000000;
    //二维码颜色
    private static final int WHITE = 0xFFFFFFFF;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 400;
    private static final String IMAGE_TYPE = "png";

    public static void create(String text, HttpServletResponse response) {
        Map<EncodeHintType, String> his = new HashMap<>();
        //设置编码字符集
        his.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            //1、生成二维码
            BitMatrix encode = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, his);

            //2、获取二维码宽高
            int codeWidth = encode.getWidth();
            int codeHeight = encode.getHeight();

            //3、将二维码放入缓冲流
            BufferedImage image = new BufferedImage(codeWidth, codeHeight, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < codeWidth; i++) {
                for (int j = 0; j < codeHeight; j++) {
                    //4、循环将二维码内容定入图片
                    image.setRGB(i, j, encode.get(i, j) ? BLACK : WHITE);
                }
            }
            OutputStream outPutImage = response.getOutputStream();
            //5、将二维码写入图片
            ImageIO.write(image, IMAGE_TYPE, outPutImage);
            outPutImage.flush();
        } catch (WriterException e) {
            e.printStackTrace();
            LogRecord.error("二维码生成失败");
        } catch (IOException e) {
            e.printStackTrace();
            LogRecord.error("生成二维码图片失败");
        }
    }
}

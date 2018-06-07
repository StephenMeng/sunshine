package team.stephen.sunshine.util;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by stephen on 2018/3/18.
 */
public class SVMUtil {
    private static final String baseDir = "C:\\Users\\Stephen\\Desktop\\sunshine\\weibo\\code\\svm\\";
    private static final String trainFilePath = baseDir + "train\\train-%s.txt";
    private static final String modelFilePath = baseDir + "model\\model-%s.txt";
    private static final String testFilePath = baseDir + "test\\testdata.txt";
    private static final String predictFilePath = baseDir + "predict\\predict-%s.txt";

    /**
     * 输出libsvm的标准格式的文件
     *
     * @param map
     * @param testFilePath
     * @param tokenList
     * @param asTestData
     * @param classifyType
     */
//    public static void outPutSVMStandardData(Map<String, Integer> map, String testFilePath,
//                                             List<Token> tokenList, boolean asTestData, int classifyType) {
//        File tagFile = new File(String.format(testFilePath));
//        StringBuilder stringBuilder = new StringBuilder();
//        tokenList.forEach(token -> {
//            if (map.get(token.getWord()) != null) {
//                stringBuilder.append(" " + map.get(token.getWord()) + ":");
//                double weight = token.getWeight();
//                stringBuilder.append(weight);
//            }
//        });
//        if (!StringUtils.isNull(stringBuilder.toString())) {
//            try {
//                if (asTestData) {
//
//                    Files.write("-1" + stringBuilder.toString() + "\r\n", tagFile, Charsets.UTF_8);
//                } else {
//                    Files.append(String.valueOf(classifyType) + stringBuilder.toString() + "\r\n", tagFile, Charsets.UTF_8);
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 训练模型
     *
     * @param tag
     * @throws IOException
     */
    public static void trainModel(String tag) throws IOException {

        String[] arg = getTrainModelPath(tag);
        SvmTrain.main(arg);
    }

    private static String[] getTrainModelPath(@RequestParam("tag") String tag) {
        return new String[]{
                "-g", "2.0", "-c", "32", "-t", "0", "-m", "500.0", "-h", "0", "-b", "1",
                //训练集
                String.format(trainFilePath, tag),
                String.format(modelFilePath, tag)};
    }

    /**
     * 预测模型
     *
     * @param tag
     */
    public static void predict(String tag) {
        String[] parg = {String.format(testFilePath), //测试数据
                String.format(modelFilePath, tag), // 调用训练模型
                String.format(predictFilePath, tag),
                "-g", "2.0", "-c", "100", "-t", "0", "-m", "500.0", "-h", "0",
                "-b", "1", "-v", "5"}; //预测结果
        try {
            SvmPredict.main(parg);  //调用
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double genSvmResult(String tag) {
        List<String> result = null;
        try {
            result = Files.readLines(new File(String.format(predictFilePath, tag)), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
        try {
            Double r = Double.parseDouble(result.get(1).split(" ")[2]);
            return r;
        } catch (Exception e) {
           try{
               Double r = Double.parseDouble(result.get(1).split(" ")[1]);
           }catch (Exception e2){
               e2.printStackTrace();
           }
        }
        return -1;
    }
}

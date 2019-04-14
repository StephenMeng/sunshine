package controller;


import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.io.Files;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import team.stephen.sunshine.util.common.LogRecord;

import java.io.*;
import java.util.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ClusterTest {
    @Test
    public void testHCluster() throws IOException {
        String filesPath = "C:\\Users\\stephen\\Desktop\\area";
        File[] files = new File(filesPath).listFiles();
        Map<String, Integer> words = new HashMap<>();
        for (File file : files) {
            if (file.getName().contains("txt")) {
                List<String> lines = Files.readLines(file, Charsets.UTF_8);
                for (String line : lines) {
                    String word = line.split("\t")[0];
                    Integer count = Integer.parseInt(line.split("\t")[1]);
                    if (!words.containsKey(word)) {
                        words.put(word, count);
                    } else {
                        words.put(word, words.get(word) + count);
                    }
                }
            }
        }
        List<String> ws = new ArrayList<>();
        int th = 20;
        for (Map.Entry<String, Integer> map : words.entrySet()) {
            if (map.getValue() > th && map.getKey().length() > 1) {
                ws.add(map.getKey());
            }
        }
        LogRecord.print(ws);

        List<List<String>> result = new ArrayList<>();
        for (String w : ws) {
            List<String> item = Lists.newArrayList(w);
            result.add(item);
        }

        int clusterNum = 10;

        while (result.size() > clusterNum) {
            double max = -1;
            int startIndex = -1;
            int endIndex = -1;
            for (int i = 0; i < result.size(); i++) {
                for (int j = i + 1; j < result.size(); j++) {
                    double score = computeScore(result.get(i), result.get(j));
                    if (score >= max) {
                        startIndex = i;
                        endIndex = j;
                        max = score;
                    }
                }
            }
            List<String> endList = result.get(endIndex);
            List<String> startList = result.get(startIndex);
            startList.addAll(endList);
            result.remove(endIndex);
        }
        LogRecord.print(result);
        String outPutPath = "C:\\Users\\stephen\\Desktop\\area-result.txt";
        BufferedWriter bufferedWriter = Files.newWriter(new File(outPutPath), Charsets.UTF_8);
        for (List<String> stringList : result) {
            bufferedWriter.write(Joiner.on(";").join(stringList) + "\r\n");

        }
        bufferedWriter.close();

    }

    private double computeScore(List<String> a, List<String> b) {
        if (a == null || b == null || a.size() == 0 || b.size() == 0) {
            return 0;
        }
        if (a.size() <= b.size()) {
            int count = 0;
//            for (String w : a) {
//                if(b.contains(w)){
//                    count++;
//                }
//            }
//            return count / (double) a.size();

            String astr = Joiner.on("").join(a);
            String bstr = Joiner.on("").join(b);
            for (int i = 0; i < astr.length(); i++) {
                String tmp = astr.substring(i, i + 1);
                if (bstr.contains(tmp)) {
                    count++;
                }
            }
            return count / (double) bstr.length();
        } else {
            int count = 0;
//            for (String w : b) {
//                if(a.contains(w)){
//                    count++;
//                }
//            }
//            return count / (double) b.size();

            String astr = Joiner.on("").join(a);
            String bstr = Joiner.on("").join(b);
            for (int i = 0; i < bstr.length(); i++) {
                String tmp = bstr.substring(i, i + 1);
                if (astr.contains(tmp)) {
                    count++;
                }
            }
            return count / (double) astr.length();
        }
    }
 
}

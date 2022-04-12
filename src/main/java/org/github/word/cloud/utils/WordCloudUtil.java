package org.github.word.cloud.utils;

import com.kennycason.kumo.*;
import com.kennycason.kumo.bg.PixelBoundryBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * 词云工具类
 *
 * @author JHY
 * @date 2022/3/29 15:00
 */
public class WordCloudUtil {

    private static final Random RANDOM = new Random();
    private static final List<String> BACKGROUND = new ArrayList<>();

    static {
        try {
            Enumeration<URL> urls = WordCloudUtil.class.getClassLoader().getResources("backgrounds");
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                File file = new File(url.toURI());
                File[] files = file.listFiles();

                for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
                    String absolutePath = files[i].getAbsolutePath();
                    BACKGROUND.add(absolutePath);
                }
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static synchronized File gen(List<String> texts) {
        //建立词频分析器，设置词频，以及词语最短长度，此处的参数配置视情况而定即可
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        // 返回字数限制
        frequencyAnalyzer.setWordFrequenciesToReturn(600);
        // 最短字符
        frequencyAnalyzer.setMinWordLength(2);
        // 引入中文解析器
        frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
        // 设置词汇文本
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(texts);
        // 生成图片尺寸 width 1920  height 1080
        Dimension dimension = new Dimension(600, 600);
        // 生产词云形状
        WordCloud wordCloud = new PolarWordCloud(dimension, CollisionMode.PIXEL_PERFECT, PolarBlendMode.BLUR);
        // 词与词的间距
        wordCloud.setPadding(5);
        // 设置中文字体样式
        Font font = new Font("STSong-Light", Font.ITALIC, 20);
        // 设置背景颜色
        wordCloud.setBackgroundColor(new Color(255, 255, 255));

        wordCloud.setPadding(5);
        // 生成字体
        wordCloud.setKumoFont(new KumoFont(font));
        try {
            wordCloud.setBackground(new PixelBoundryBackground(BACKGROUND.get(RANDOM.nextInt(BACKGROUND.size()))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 生成字体颜色
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 30, 30));
        wordCloud.setFontScalar(new LinearFontScalar(10, 40));
        wordCloud.build(wordFrequencies);

        // 生成图片地址
        wordCloud.writeToFile("wy.png");

        return new File("wy.png");
    }
}

package org.github.word.cloud;

import org.github.palace.bot.core.plugin.Plugin;

/**
 * @author JHY
 * @date 2022/3/30 16:01
 */
public class App extends Plugin {

    public App() {
        super("1.0-SNAPSHOT", "词云", "在QQ群中查看个人词云");
    }

    @Override
    public void onLoad() {
        super.register(new WordCloudCommand());
    }

}
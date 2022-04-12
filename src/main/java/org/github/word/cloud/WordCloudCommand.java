package org.github.word.cloud;

import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import org.github.palace.bot.core.annotation.CommandHandler;
import org.github.palace.bot.core.cli.CommandSender;
import org.github.palace.bot.core.cli.SimpleCommand;
import org.github.word.cloud.utils.WordCloudUtil;
import org.github.palace.bot.data.MybatisContext;
import org.github.palace.bot.data.message.mapper.MessageMapper;

import java.io.File;
import java.util.List;

/**
 * @author JHY
 * @date 2022/3/30 16:35
 */
public class WordCloudCommand extends SimpleCommand {
    public WordCloudCommand() {
        super("查看个人词云", null, false, "查看个人词云");
    }

    @CommandHandler
    public void handler(CommandSender commandSender, MessageSource messageSource, PlainText plainText) {
        MessageMapper messageMapper = MybatisContext.INSTANCE.get(MessageMapper.class);
        List<String> messages = messageMapper.selectMessages(commandSender.getSubject().getId(), messageSource.getFromId());

        File file = WordCloudUtil.gen(messages);
        Image image = commandSender.getSubject().uploadImage(ExternalResource.create(file));
        commandSender.sendMessage(new At(messageSource.getFromId()).plus(" ").plus(image));
    }

}

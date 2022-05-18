package org.github.word.cloud;

import net.mamoe.mirai.contact.MemberPermission;
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
import java.util.Calendar;
import java.util.List;

/**
 * @author JHY
 * @date 2022/3/30 16:35
 */
public class WordCloudCommand extends SimpleCommand {
    public WordCloudCommand() {
        super("查看词云", MemberPermission.MEMBER, false, "查看词云");
    }

    @CommandHandler
    public void handler(CommandSender commandSender) {
        MessageMapper messageMapper = MybatisContext.INSTANCE.get(MessageMapper.class);

        Calendar calendar = Calendar.getInstance();
        long end = calendar.getTimeInMillis();

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        long start = calendar.getTimeInMillis();

        long formId = commandSender.getUser().getId();
        List<String> messages = messageMapper.selectMessagesByCreateAt(commandSender.getSubject().getId(), formId, start, end);
        if (!messages.isEmpty()) {
            At at = new At(formId);
            commandSender.sendMessage(" 您一周内发言共" + messages.size() + "条, 请稍等词云正在制作中...");

            File file = WordCloudUtil.gen(messages);
            Image image = commandSender.getSubject().uploadImage(ExternalResource.create(file));
            commandSender.sendMessage(at.plus(" ").plus(image));
        }
    }

}

package vcg.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

public class Ping extends PureCommand {
    public Ping(String name) {
        super(name);
    }

    @Override
    public void onCommand(MessageChain source, GroupMessageEvent event) {
        MessageChain msg = new MessageChainBuilder()
                .append(new QuoteReply(source))
                .append(new At(event.getSender().getId()))
                .append("pong")
                .build();
        event.getSubject().sendMessage(msg);
    }
}

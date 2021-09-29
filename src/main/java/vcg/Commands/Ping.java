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
    public String info() {
        return super.info()+" -> 机器人会@你并回复'pong'";
    }

    @Override
    public void onCommand(GroupMessageEvent event) {
        MessageChain msg = new MessageChainBuilder()
                .append(new QuoteReply(event.getSource()))
                .append(new At(event.getSender().getId()))
                .append("pong")
                .build();
        event.getSubject().sendMessage(msg);
    }
}

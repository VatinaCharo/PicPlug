package vcg.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.QuoteReply;

public class Help extends PureCommand{
    public Help() {
        super("help");
    }

    @Override
    public String info() {
        return super.info()+" -> 机器人会@你并回复帮助信息";
    }

    @Override
    public void onCommand(GroupMessageEvent event) {
        MessageChainBuilder mb = new MessageChainBuilder()
                .append(new QuoteReply(event.getSource()))
                .append(new At(event.getSender().getId()))
                .append("\n")
                .append("Help Info Manuel\n")
                .append("--------------------\n");
        for (Command c :
                CommandsManager.COMMAND_LIST) {
            mb.append(c.info()).append("\n");
        }
        event.getSubject().sendMessage(mb.build());
    }
}

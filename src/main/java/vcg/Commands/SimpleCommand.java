package vcg.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * Java示例
 * ```java
 * import net.mamoe.mirai.message.data.MessageChain;
 * import net.mamoe.mirai.message.data.MessageChainBuilder;
 * import net.mamoe.mirai.message.data.QuoteReply;
 *
 * public class EchoSimpleCommand extends SimpleCommand {
 *
 *     public EchoSimpleCommand(String name) {
 *         super(name);
 *     }
 *
 *     @Override
 *     public void onCommand(MessageChain source, GroupMessageEvent event) {
 *         return new MessageChainBuilder()
 *                 .append(new QuoteReply(source))
 *                 .append(new At(event.getSender().getId()))
 *                 .append("Echo:\n")
 *                 .append("\"")
 *                 .append(this.getContext())
 *                 .append("\"")
 *                 .build();
 *     }
 * }
 * ```
 */
public abstract class SimpleCommand {
    private final String name;
    private String context;

    public SimpleCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public abstract void onCommand(MessageChain source, GroupMessageEvent event);

}

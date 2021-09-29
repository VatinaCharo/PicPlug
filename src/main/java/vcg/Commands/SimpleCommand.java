package vcg.Commands;

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
 *     public void onCommand(GroupMessageEvent event) {
 *         return new MessageChainBuilder()
 *                 .append(new QuoteReply(event.getSource()))
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
public abstract class SimpleCommand implements Command{
    private final String name;
    private String context;

    public SimpleCommand(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}

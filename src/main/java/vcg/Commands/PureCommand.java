package vcg.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

public abstract class PureCommand {
        private final String name;

        public PureCommand(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public abstract void onCommand(MessageChain source, GroupMessageEvent event);
}

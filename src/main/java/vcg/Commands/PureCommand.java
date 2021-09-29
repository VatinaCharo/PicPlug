package vcg.Commands;


public abstract class PureCommand implements Command{
        private final String name;

        public PureCommand(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
}

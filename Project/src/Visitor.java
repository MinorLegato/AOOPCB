
public interface Visitor {
		public String visit(Component component);
		public String visit(RenderBoard renderBoard);
		public String visit(RenderComponent renderComponent);
		public String visit(RenderWire renderWire);
}

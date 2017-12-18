package nari.model.symbol;

public interface SymbolProvider {

	public SymbolSelector get();
	
	public int getPriority();
	
	public boolean selfCheck();
}

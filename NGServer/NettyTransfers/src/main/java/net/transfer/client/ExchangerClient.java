package net.transfer.client;

public interface ExchangerClient extends Client ,ExchangerChannel{
	
	public void increament();
	
	public void decrement();
	
	public int ref();
}

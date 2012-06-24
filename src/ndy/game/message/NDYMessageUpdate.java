package ndy.game.message;

public class NDYMessageUpdate extends NDYMessage {
	protected long mInterval; // dt in ms

	public NDYMessageUpdate() {
		super("update");
	}
	
	public long getInterval() {
		return mInterval;
	}
	
	public void setInterval(long dt) {
		mInterval = dt;
	}
}

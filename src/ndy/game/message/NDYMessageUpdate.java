package ndy.game.message;

public class NDYMessageUpdate extends NDYMessage {
	protected float mInterval;

	public NDYMessageUpdate() {
		super("update");
	}
	
	public float getInterval() {
		return mInterval;
	}
	
	public void setInterval(float dt) {
		mInterval = dt;
	}
}

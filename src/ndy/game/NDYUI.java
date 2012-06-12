package ndy.game;

public class NDYUI {
	public static String FOCUS_NONE = "none";
	public static String FOCUS_MAINSAIL = "mainsail";
	public static String FOCUS_RUDDER = "rudder";

	private String mFocus = FOCUS_NONE;
	
	public void setFocus(String focus) {
		mFocus = focus;
	}
	
	public String getFocus() {
		return mFocus;
	}
}

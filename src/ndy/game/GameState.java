package ndy.game;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.System;


abstract public class GameState {
	public static final int LOADING = 1;
	public static final int RUNNING = 2;
	
	abstract public void dispatchMessage(Message m);
}

package ndy.state;

import ndy.game.Game;
import ndy.game.message.Message;
import ndy.game.message.UpdateMessage;
import ndy.game.system.System;

public class RunningState extends GameState {
	public void dispatchMessage(Message m) {
		if (m.getType() == Message.T_UPDATE) {
			Game.instance.time += ((UpdateMessage) m).getInterval();
		}

		for (System s : Game.instance.systems.values()) {
			s.dispatchMessage(m);
		}
	}
}

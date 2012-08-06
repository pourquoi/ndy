package ndy.game.system;

import java.util.ArrayList;

import ndy.game.Game;
import ndy.game.component.Component;
import ndy.game.message.InputMessage;
import ndy.game.message.Message;

public class InputSystem extends System {
	public static final String name = "input";
	public ArrayList<InputMessage> inputBuffer = new ArrayList<InputMessage>();

	@Override
	public void dispatchMessage(Message m) {
		if (m.getType() == Message.T_INPUT) {
			for (Component c : components) {
				InputMessage msg = (InputMessage) m;
				if (c.parent == msg.actor)
					c.processMessage(m);
			}
		}
	}

	public void dispatchInputs() {
		for (InputMessage msg : inputBuffer) {
			dispatchMessage(msg);
		}

		inputBuffer.clear();
	}
}

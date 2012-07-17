package ndy.game;

import java.util.ArrayList;

import ndy.game.message.NDYMessageInput;

public class NDYInput {
	public ArrayList<NDYMessageInput> inputs = new ArrayList<NDYMessageInput>();

	public void move(float dx, float dy) {
		NDYMessageInput m = new NDYMessageInput("input");
		m.type = NDYMessageInput.T_MOVE;
		m.dx = dx;
		m.dy = dy;
		inputs.add(m);
	}

	public void down(float x, float y) {
		NDYMessageInput m = new NDYMessageInput("input");
		m.type = NDYMessageInput.T_DOWN;
		m.x = x;
		m.y = y;
		inputs.add(m);
	}

	public void up(float x, float y) {
		NDYMessageInput m = new NDYMessageInput("input");
		m.type = NDYMessageInput.T_UP;
		m.x = x;
		m.y = y;
		inputs.add(m);
	}
}

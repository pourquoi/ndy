package ndy.game.component;

import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageRender;
import ndy.game.shader.NDYProgram;

public class NDYComponentProgram extends NDYComponent {
	NDYProgram program;
	
	protected float[] modelMatrix = new float[16];
	protected float[] translationMatrix = new float[16];
	protected float[] rotationMatrix = new float[16];

	public NDYComponentProgram(NDYProgram program) {
		super("program");
		this.program = program;
	}
}

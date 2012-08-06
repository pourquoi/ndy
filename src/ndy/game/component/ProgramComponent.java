package ndy.game.component;

import ndy.game.shader.Program;

public class ProgramComponent extends Component {
	Program program;
	
	protected float[] modelMatrix = new float[16];
	protected float[] translationMatrix = new float[16];
	protected float[] rotationMatrix = new float[16];

	public ProgramComponent(Program program) {
		super("program");
		this.program = program;
	}
}

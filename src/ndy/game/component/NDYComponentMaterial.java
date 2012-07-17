package ndy.game.component;

import ndy.game.material.NDYMaterial;

public class NDYComponentMaterial extends NDYComponent {
	public NDYMaterial material;

	public NDYComponentMaterial(NDYMaterial material, int index) {
		super("material"+index);
		this.material = material;
	}
}

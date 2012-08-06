package ndy.game.component;

import ndy.game.material.Material;

public class MaterialComponent extends Component {
	public Material material;

	public MaterialComponent(Material material, int index) {
		super("material"+index);
		this.material = material;
	}
}

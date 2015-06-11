package ndy.game.mesh;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;

public class LoaderPhysics extends Loader3DS {
	private Body _body;
	private FixtureDef _fd;
	
	public LoaderPhysics(Body body, FixtureDef fd) {
		_body = body;
		_fd = fd;
	}
	
	protected void processObject() {
		PolygonShape shape = new PolygonShape();
		Vec2[] vertices = new Vec2[3];
		
		for (LoaderFace f : _faces) {
			short[] vindexes = { f.a, f.b, f.c };
			int i = 0;
			for (short vi : vindexes) {
				vertices[i] = new Vec2();
				vertices[i].x = _vertices.get(vi).x;
				vertices[i].y = _vertices.get(vi).z;
				i++;
			}
			shape.set(vertices, 3);
			
			_fd.shape = shape;
			_body.createFixture(_fd);
		}
	}
}

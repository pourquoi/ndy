package ndy.game;

import ndy.game.component.NDYComponentCinematicSailboat;
import ndy.game.component.NDYComponentTransformation;

public class NDYInput {
	public void move(float dx, float dy) {
		NDYUI ui = NDYWorld.current.getUI();
		if( ui != null ) {
			NDYActor r = NDYWorld.current.getRacer();
			if( r != null ) {
				if( ui.getFocus() == NDYUI.FOCUS_MAINSAIL ) {
					NDYComponentCinematicSailboat b = (NDYComponentCinematicSailboat)r.findComponent("cinematic");
					if( b != null ) {
						b.setMainSailRot(b.getMainSailRot()+dx);
					}
				} else if( ui.getFocus() == NDYUI.FOCUS_RUDDER ) {
					NDYComponentTransformation trans = (NDYComponentTransformation)r.findComponent("transformation");
					if( trans != null ) trans.getRot().y -= dx;
				}
			}
		}
		
		NDYCamera c = NDYWorld.current.getCamera();
		if( c != null ) c.getPos().y += dy;
	}
	
	public void down(float x, float y) {
		NDYUI ui = NDYWorld.current.getUI();
		NDYCamera c = NDYWorld.current.getCamera();
		
		if( ui != null && c != null && c.getHeight() != 0 ) {
			float h = c.getHeight();
			if( y > h - h/8 ) {
				NDYWorld.current.getUI().setFocus(NDYUI.FOCUS_RUDDER);
			} else {
				NDYWorld.current.getUI().setFocus(NDYUI.FOCUS_MAINSAIL);
			}
		}
	}
	
	public void up(float x, float y) {
		NDYUI ui = NDYWorld.current.getUI();
		
		if( ui != null ) {
			NDYWorld.current.getUI().setFocus(NDYUI.FOCUS_NONE);
		}
	}
}

package ndy.game.actor;

import ndy.game.component.NDYComponentPhysics;
import ndy.game.component.NDYComponentPhysicsSailboat;
import ndy.game.component.NDYComponentTransformation;
import ndy.game.message.NDYMessage;
import ndy.game.message.NDYMessageInput;
import ndy.game.message.NDYMessageRender;
import ndy.game.message.NDYMessageUpdate;

public class NDYInterface extends NDYActor {
	public static int FOCUS_NONE = 0;
	public static int FOCUS_MAINSAIL = 1;
	public static int FOCUS_RUDDER = 2;

	public int mFocus = FOCUS_NONE;

	public NDYInterface() {
		super("ui");
	}

	@Override
	public boolean dispatchMessage(NDYMessage msg) {
		if (super.dispatchMessage(msg))
			return true;

		if (msg.getClass() == NDYMessageUpdate.class) {

		}

		if (msg.getClass() == NDYMessageRender.class) {

		}

		if (msg.getClass() == NDYMessageInput.class) {
			NDYMessageInput input = (NDYMessageInput) msg;
			switch (input.type) {
			case NDYMessageInput.T_MOVE:
				if (NDYGame.instance.mRacer != null) {
					if (NDYGame.instance.mInterface.mFocus == NDYInterface.FOCUS_MAINSAIL) {
						NDYComponentPhysicsSailboat b = (NDYComponentPhysicsSailboat) NDYGame.instance.mRacer.findComponent("physics");
						if (b != null) {
							b.mMainSailRot = b.mMainSailRot + input.dx;
						}
					} else if (NDYGame.instance.mInterface.mFocus == NDYInterface.FOCUS_RUDDER) {
						NDYComponentPhysicsSailboat b = (NDYComponentPhysicsSailboat) NDYGame.instance.mRacer.findComponent("physics");
						if (b != null) {
							b.mRudderRot = b.mRudderRot + input.dx;
						}
					}
				}
				if (NDYGame.instance.mInterface.mFocus == NDYInterface.FOCUS_NONE) {
					NDYCamera c = NDYGame.instance.mCamera;
					if (c != null)
						c.mPos.y += input.dy;
					if (c.mPos.y < 5.f)
						c.mPos.y = 5.f;
					if (c.mPos.y > 30.f)
						c.mPos.y = 30.f;
				}
				break;
			case NDYMessageInput.T_DOWN:
				float h = NDYGame.instance.mCamera.mHeight;
				if (input.y > h - h / 8) {
					NDYGame.instance.mInterface.mFocus = NDYInterface.FOCUS_RUDDER;
				} else {
					NDYGame.instance.mInterface.mFocus = NDYInterface.FOCUS_MAINSAIL;
				}
				break;
			case NDYMessageInput.T_UP:
				NDYGame.instance.mInterface.mFocus = NDYInterface.FOCUS_NONE;
				break;
			}
		}

		return false;
	}
}

package com.destrostudios.cards.frontend.application;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class JMonkeyUtil {

    public static Quaternion getQuaternion_X(float degrees) {
        return getQuaternion(degrees, Vector3f.UNIT_X);
    }

    public static Quaternion getQuaternion_Y(float degrees) {
        return getQuaternion(degrees, Vector3f.UNIT_Y);
    }

    public static Quaternion getQuaternion_Z(float degrees) {
        return getQuaternion(degrees, Vector3f.UNIT_Z);
    }

    public static Quaternion getQuaternion(float degrees, Vector3f axis) {
        return new Quaternion().fromAngleAxis(((degrees / 360) * (2 * FastMath.PI)), axis);
    }
}

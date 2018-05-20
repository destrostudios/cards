package com.destrostudios.cards.frontend.cardgui.transformations;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

public class ConstantRotationTransformation extends ConstantTransformation<Quaternion> {

    public ConstantRotationTransformation() {
        this(new Quaternion());
    }

    public ConstantRotationTransformation(Quaternion value) {
        super(value);
    }

    @Override
    public void setValue(Quaternion destinationValue, Quaternion sourceValue) {
        destinationValue.set(sourceValue);
    }

    // Helpers

    public static ConstantRotationTransformation x(float radian) {
        return new ConstantRotationTransformation(new Quaternion().fromAngleAxis(radian, Vector3f.UNIT_X));
    }

    public static ConstantRotationTransformation y(float radian) {
        return new ConstantRotationTransformation(new Quaternion().fromAngleAxis(radian, Vector3f.UNIT_Y));
    }

    public static ConstantRotationTransformation z(float radian) {
        return new ConstantRotationTransformation(new Quaternion().fromAngleAxis(radian, Vector3f.UNIT_Z));
    }

    private static ConstantRotationTransformation get(Vector3f axis, float radian) {
        return new ConstantRotationTransformation(new Quaternion().fromAngleAxis(radian, axis));
    }
}

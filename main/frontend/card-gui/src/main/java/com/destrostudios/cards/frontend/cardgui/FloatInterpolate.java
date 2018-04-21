package com.destrostudios.cards.frontend.cardgui;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 *
 * @author Carl
 */
public class FloatInterpolate {

    public static Vector3f get(Vector3f currentValue, Vector3f targetValue, float speed, float lastTimePerFrame) {
        Vector3f difference = targetValue.subtract(currentValue);
        float differenceLength = difference.length();
        if (differenceLength > 0) {
            float movedDistance = (speed * lastTimePerFrame);
            if (movedDistance < differenceLength) {
                return currentValue.add(difference.normalizeLocal().multLocal(movedDistance));
            }
        }
        return targetValue;
    }
    
    public static Quaternion get(Quaternion currentValue, Quaternion targetValue, float speed, float lastTimePerFrame) {
        if (!currentValue.equals(targetValue)) {
            float x = get(currentValue.getX(), targetValue.getX(), speed, lastTimePerFrame);
            float y = get(currentValue.getY(), targetValue.getY(), speed, lastTimePerFrame);
            float z = get(currentValue.getZ(), targetValue.getZ(), speed, lastTimePerFrame);
            float w = get(currentValue.getW(), targetValue.getW(), speed,  lastTimePerFrame);
            return new Quaternion(x, y, z, w);
        }
        return targetValue;
    }
    
    public static float get(float currentValue, float targetValue, float speed, float lastTimePerFrame) {
        if (currentValue != targetValue) {
            float difference = (targetValue - currentValue);
            float movedDistance = (speed * lastTimePerFrame);
            if (movedDistance < Math.abs(difference)) {
                return (currentValue + (Math.signum(difference) * movedDistance));
            }
        }
        return targetValue;
    }
}

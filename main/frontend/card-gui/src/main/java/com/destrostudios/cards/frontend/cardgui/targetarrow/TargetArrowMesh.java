package com.destrostudios.cards.frontend.cardgui.targetarrow;

import com.destrostudios.cards.frontend.cardgui.Util;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import java.util.LinkedList;

/**
 *
 * @author Carl
 */
public class TargetArrowMesh extends Mesh{

    public TargetArrowMesh(int resolution, float width){
        this.resolution = resolution;
        this.width = width;
    }
    private int resolution;
    private float width;
    private short currentIndex = 0;
    private LinkedList<Short> indices = new LinkedList<>();
    private LinkedList<Float> positions = new LinkedList<>();
    private LinkedList<Float> normals = new LinkedList<>();
    private LinkedList<Float> textureCoordinates = new LinkedList<>();

    // TODO: Update only positions buffer instead of everything...
    public void updatePositions(Vector3f sourceLocation, Vector3f targetLocation) {
        currentIndex = 0;
        indices.clear();
        positions.clear();
        normals.clear();
        textureCoordinates.clear();
        float distance = sourceLocation.distance(targetLocation);
        float interval = (distance / resolution);
        float halfWidth = (width / 2);
        float x = 0;
        float z = 0;
        for (int i=0;i<resolution;i++) {
            //Triangle 1
            indices.add((short) (currentIndex + 1));
            indices.add(currentIndex);
            indices.add((short) (currentIndex + 2));
            currentIndex += 3;
            textureCoordinates.add(0f);
            textureCoordinates.add(1f);
            textureCoordinates.add(1f);
            textureCoordinates.add(1f);
            textureCoordinates.add(0.5f);
            textureCoordinates.add(0f);
            positions.add(x - halfWidth);
            positions.add(0f);
            positions.add(z);
            positions.add(x + halfWidth);
            positions.add(0f);
            positions.add(z);
            positions.add(x + halfWidth);
            positions.add(0f);
            positions.add(z + interval);
            for(int r=0;r<3;r++){
                normals.add(0f);
                normals.add(0f);
                normals.add(1f);
            }
            //Triangle 2
            indices.add((short) (currentIndex + 1));
            indices.add(currentIndex);
            indices.add((short) (currentIndex + 2));
            currentIndex += 3;
            textureCoordinates.add(0f);
            textureCoordinates.add(1f);
            textureCoordinates.add(1f);
            textureCoordinates.add(1f);
            textureCoordinates.add(0.5f);
            textureCoordinates.add(0f);
            positions.add(x - halfWidth);
            positions.add(0f);
            positions.add(z);
            positions.add(x + halfWidth);
            positions.add(0f);
            positions.add(z + interval);
            positions.add(x - halfWidth);
            positions.add(0f);
            positions.add(z + interval);
            for(int r=0;r<3;r++){
                normals.add(0f);
                normals.add(0f);
                normals.add(1f);
            }
            z += interval;
        }
        setBuffer(Type.Index, 3, Util.convertToArray_Short(indices));
        setBuffer(Type.Position, 3, Util.convertToArray_Float(positions));
        setBuffer(Type.Normal, 3, Util.convertToArray_Float(normals));
        setBuffer(Type.TexCoord, 2, Util.convertToArray_Float(textureCoordinates));
        updateBound();
    }
}

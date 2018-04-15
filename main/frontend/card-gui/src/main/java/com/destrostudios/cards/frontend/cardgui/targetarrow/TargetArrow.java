package com.destrostudios.cards.frontend.cardgui.targetarrow;

import com.destrostudios.cards.frontend.cardgui.JMonkeyUtil;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;

/**
 *
 * @author Carl
 */
public class TargetArrow {
    
    public TargetArrow(AssetManager assetManager) {
        targetArrowMesh = new TargetArrowMesh(10, 1);
        geometry = new Geometry("targetArrow", targetArrowMesh);
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color",  ColorRGBA.Blue);
        material.getAdditionalRenderState().setWireframe(true);
        material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        geometry.setMaterial(material);
        updateGeometry(new Vector3f(), new Vector3f());
    }
    private TargetArrowMesh targetArrowMesh;
    private Geometry geometry;
    
    public void updateGeometry(Vector3f sourceLocation, Vector3f targetLocation) {
        geometry.setLocalTranslation(sourceLocation);
        JMonkeyUtil.setLocalRotation(geometry, targetLocation.subtract(sourceLocation));
        targetArrowMesh.updatePositions(sourceLocation, targetLocation);
    }

    public Geometry getGeometry() {
        return geometry;
    }
}

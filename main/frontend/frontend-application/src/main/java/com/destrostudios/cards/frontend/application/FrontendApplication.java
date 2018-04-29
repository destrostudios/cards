package com.destrostudios.cards.frontend.application;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;

public class FrontendApplication extends SimpleApplication {

    public static void main(String[] args){
        FrontendApplication app = new FrontendApplication();
        app.start();
    }

    public FrontendApplication() {
        loadSettings();
    }

    private void loadSettings(){
        settings = new AppSettings(true);
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Cards");
        setShowSettings(false);
    }

    @Override
    public void simpleInitApp() {
        Geometry geometry = new Geometry("Box", new Box(1, 1, 1));
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
        geometry.setMaterial(material);
        rootNode.attachChild(geometry);
    }
}
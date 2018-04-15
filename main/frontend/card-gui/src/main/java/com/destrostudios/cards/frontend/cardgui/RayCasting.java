package com.destrostudios.cards.frontend.cardgui;

import com.jme3.app.Application;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;

/**
 *
 * @author Carl
 */
public class RayCasting {

    public RayCasting(Application application) {
        this.application = application;
    }
    private Application application;

    public CollisionResults getResults_Cursor(Spatial spatial){
        return getResults_Screen(spatial, application.getInputManager().getCursorPosition());
    }
    
    public CollisionResults getResults_Screen(Spatial spatial, Vector2f screenLocation){
        Vector3f cursorRayOrigin = application.getCamera().getWorldCoordinates(screenLocation, 0);
        Vector3f cursorRayDirection = application.getCamera().getWorldCoordinates(screenLocation, 1).subtractLocal(cursorRayOrigin).normalizeLocal();
        return getResults(spatial, new Ray(cursorRayOrigin, cursorRayDirection));
    }
    
    public CollisionResults getResults_ScreenCenter(Application simpleApplication, Spatial spatial){
        AppSettings settings = simpleApplication.getContext().getSettings();
        Vector3f origin = simpleApplication.getCamera().getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.0f);
        Vector3f direction = simpleApplication.getCamera().getWorldCoordinates(new Vector2f((settings.getWidth() / 2), (settings.getHeight() / 2)), 0.3f);
        direction.subtractLocal(origin).normalizeLocal();
        return getResults(spatial, new Ray(origin, direction));
    }
    
    private CollisionResults getResults(Spatial spatial, Ray ray){
        CollisionResults results = new CollisionResults();
        spatial.collideWith(ray, results);
        return results;
    }
}

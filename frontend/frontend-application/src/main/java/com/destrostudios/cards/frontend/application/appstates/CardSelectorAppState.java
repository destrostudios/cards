package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.destrostudios.cards.frontend.application.gui.GuiUtil;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Label;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class CardSelectorAppState extends MyBaseAppState {

    public CardSelectorAppState(String description, List<int[]> validTargets, Function<Integer, CardModel> getCardModel, Consumer<int[]> onSubmit) {
        this.description = description;
        this.validTargets = validTargets;
        this.getCardModel = getCardModel;
        this.onSubmit = onSubmit;
    }
    private String description;
    private List<int[]> validTargets;
    private Function<Integer, CardModel> getCardModel;
    private Consumer<int[]> onSubmit;
    protected Node rootNode;
    protected Node guiNode;
    private HashMap<CardModel, Integer> cardModelsToTargets;
    private Button buttonSubmit;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        rootNode = new Node();
        mainApplication.getRootNode().attachChild(rootNode);

        guiNode = new Node();
        mainApplication.getGuiNode().attachChild(guiNode);

        initSelector();
        initGui();
    }

    private void initSelector() {
        HashSet<Integer> allTargets = new HashSet<>();
        for (int[] targets : validTargets) {
            for (int target : targets) {
                allTargets.add(target);
            }
        }

        cardModelsToTargets = new HashMap<>();
        List<CardModel> cardModels = allTargets.stream()
                .map(target -> {
                    CardModel cardModel = new CardModel();
                    cardModel.set(getCardModel.apply(target));
                    cardModel.setPlayable(false);
                    cardModelsToTargets.put(cardModel, target);
                    return cardModel;
                })
                .collect(Collectors.toList());

        initSelector(cardModels);
    }

    protected abstract void initSelector(List<CardModel> cardModels);

    private void initGui() {
        Geometry background = new Geometry("background", new Quad(1, 1));
        Material materialBackground = new Material(mainApplication.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        materialBackground.setColor("Color", new ColorRGBA(0, 0, 0, 0.7f));
        materialBackground.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        background.setMaterial(materialBackground);
        background.setLocalTranslation(-6, 1, 5);
        background.setLocalRotation(new Quaternion().fromAngleAxis(-1 * FastMath.HALF_PI, Vector3f.UNIT_X));
        background.setLocalScale(12, 7, 1);
        rootNode.attachChild(background);

        int width = mainApplication.getContext().getSettings().getWidth();
        int height = mainApplication.getContext().getSettings().getHeight();
        initGui(width, height);
    }

    protected void initGui(int width, int height) {
        Label labelDescription = new Label(description);
        labelDescription.setFontSize(24);
        float labelX = ((width / 2f) - (labelDescription.getPreferredSize().getX() / 2));
        float labelY = (height - 163);
        labelDescription.setLocalTranslation(labelX, labelY, 0);
        labelDescription.setColor(ColorRGBA.White);
        guiNode.attachChild(labelDescription);

        float buttonWidth = 200;
        float buttonHeight = 100;
        float buttonGapX = 30;

        float buttonSubmitX = ((width / 2f) + (buttonGapX / 2));
        float buttonCancelX = ((width / 2f) - buttonWidth - (buttonGapX / 2));
        float buttonY = 280;

        buttonSubmit = GuiUtil.createButton("", buttonWidth, buttonHeight, _ -> onSubmit());
        buttonSubmit.setLocalTranslation(buttonSubmitX, buttonY, 0);
        guiNode.attachChild(buttonSubmit);
        updateSubmitButton();

        Button buttonCancel = GuiUtil.createButton("Cancel", buttonWidth, buttonHeight, _ -> {
            mainApplication.getStateManager().detach(this);
        });
        buttonCancel.setLocalTranslation(buttonCancelX, buttonY, 0);
        guiNode.attachChild(buttonCancel);
    }

    protected void updateSubmitButton() {
        int[] selectedTargets = getSelectedTargets();
        buttonSubmit.setText("Submit (" + selectedTargets.length + ")");
        GuiUtil.setButtonEnabled(buttonSubmit, validTargets.stream().anyMatch(targets -> ArrayUtil.equalsUnsortedAndUnique(targets, selectedTargets)));
    }

    private void onSubmit() {
        onSubmit.accept(getSelectedTargets());
        mainApplication.getStateManager().detach(this);
    }

    private int[] getSelectedTargets() {
        List<CardModel> selectedCardModels = getSelectedTargetsCardModels();
        int[] selectedTargets = new int[selectedCardModels.size()];
        int i = 0;
        for (CardModel selectedCardModel : selectedCardModels) {
            selectedTargets[i] = cardModelsToTargets.get(selectedCardModel);
            i++;
        }
        return selectedTargets;
    }

    protected abstract List<CardModel> getSelectedTargetsCardModels();

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getRootNode().detachChild(rootNode);
        mainApplication.getGuiNode().detachChild(guiNode);
    }
}

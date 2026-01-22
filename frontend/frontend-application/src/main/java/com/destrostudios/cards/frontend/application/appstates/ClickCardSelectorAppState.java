package com.destrostudios.cards.frontend.application.appstates;

import com.destrostudios.cardgui.*;
import com.destrostudios.cardgui.events.MoveCardEvent;
import com.destrostudios.cardgui.interactivities.ClickInteractivity;
import com.destrostudios.cardgui.zones.CenteredIntervalZone;
import com.destrostudios.cards.frontend.application.appstates.services.BoardUtil;
import com.destrostudios.cards.frontend.application.appstates.services.IngameCardVisualizer;
import com.destrostudios.cards.frontend.application.appstates.services.cardpainter.model.CardModel;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

import java.util.List;
import java.util.function.Consumer;

public class ClickCardSelectorAppState extends SelectorAppState {

    public ClickCardSelectorAppState(String description, List<CardModel> cardModels, Runnable onCancel, boolean hasSubmitButton, Consumer<CardModel> onSubmit) {
        super(description, onCancel, hasSubmitButton);
        this.cardModels = cardModels;
        this.onSubmit = onSubmit;
    }
    private List<CardModel> cardModels;
    private Consumer<CardModel> onSubmit;
    private BoardAppState boardAppState;

    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        Board board = new Board(BoardUtil.getDefaultSettings("click_card_selector").build());

        CenteredIntervalZone zone = new CenteredIntervalZone(getCenter(), Quaternion.IDENTITY, new Vector3f(0.83f, 1, 1));
        board.addZone(zone);

        board.registerVisualizer_ZonePosition(zonePosition -> zonePosition.getZone() == zone, IngameCardVisualizer.forIngame_InspectOrSelect());

        int i = 0;
        for (CardModel cardModel : cardModels) {
            Card<CardModel> card = new Card<>(cardModel);
            card.setInteractivity(InteractivitySource.MOUSE_LEFT, new ClickInteractivity() {

                @Override
                public void trigger(BoardObject boardObject, BoardObject boardObject1) {
                    onSubmit.accept(cardModel);
                    onSubmit();
                }
            });
            board.triggerEvent(new MoveCardEvent(card, zone, new Vector3f(i, 0, 0)));
            i++;
        }

        boardAppState = new BoardAppState(board, rootNode);
        mainApplication.getStateManager().attach(boardAppState);
    }

    @Override
    public void cleanup() {
        super.cleanup();
        mainApplication.getStateManager().detach(boardAppState);
    }
}

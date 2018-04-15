package com.destrostudios.cards.frontend.cardgui;

/**
 *
 * @author Carl
 */
public interface BoardObjectFilter {
    
    BoardObjectFilter CARD = (BoardObject boardObject) -> boardObject instanceof Card;
    BoardObjectFilter ZONE = (BoardObject boardObject) -> boardObject instanceof CardZone;
    
    static BoardObjectFilter getCompositeFilter(BoardObjectFilter... boardObjectFilters) {
        return (BoardObject boardObject) -> {
            for (BoardObjectFilter boardObjectFilter : boardObjectFilters) {
                if (!boardObjectFilter.isValid(boardObject)) {
                    return false;
                }
            }
            return true;
        };
    };
    
    static BoardObjectFilter getClassFilter(Class<? extends BoardObject> boardObjectClass) {
        return (BoardObject boardObject) -> boardObjectClass.isAssignableFrom(boardObject.getClass());
    };
    
    static BoardObjectFilter getInstanceFilter(BoardObject boardObject) {
        return (BoardObject currentBoardObject) -> currentBoardObject == boardObject;
    };

    boolean isValid(BoardObject boardObject);
}

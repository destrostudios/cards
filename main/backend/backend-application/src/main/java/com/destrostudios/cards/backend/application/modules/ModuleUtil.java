package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.rules.StartGameInfo;

public class ModuleUtil {

    public static boolean isUserInGame(StartGameInfo startGameInfo, JwtAuthenticationUser user) {
        return (startGameInfo.getPlayer1().getId() == user.id) || (startGameInfo.getPlayer2().getId() == user.id);
    }
}

package com.destrostudios.cards.backend.application.modules;

import com.destrostudios.authtoken.JwtAuthenticationUser;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;

public class ModuleUtil {

    public static boolean isUserInGame(StartGameInfo startGameInfo, JwtAuthenticationUser user) {
        for (PlayerInfo playerInfo : startGameInfo.getPlayers()) {
            if (playerInfo.getId() == user.id) {
                return true;
            }
        }
        return false;
    }
}

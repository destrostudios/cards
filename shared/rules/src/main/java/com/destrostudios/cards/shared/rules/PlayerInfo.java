package com.destrostudios.cards.shared.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerInfo {
    private long id;
    private String login;
    private List<String> libraryTemplates;
}

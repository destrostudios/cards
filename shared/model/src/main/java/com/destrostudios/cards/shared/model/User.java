package com.destrostudios.cards.shared.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class User {
    private int id;
    private String login;
    private boolean admin;
    private LocalDateTime firstLoginDate;
    private LocalDateTime lastLoginDate;
    private List<UserMode> modes;
}

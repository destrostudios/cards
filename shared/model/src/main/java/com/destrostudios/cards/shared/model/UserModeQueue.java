package com.destrostudios.cards.shared.model;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UserModeQueue {
    private int id;
    private int userId;
    private Mode mode;
    private Queue queue;
    private int games;
    private int wins;
    private int currentWinStreak;
    private int longestWinStreak;
    private LocalDateTime firstGameDate;
    private LocalDateTime lastGameDate;
}

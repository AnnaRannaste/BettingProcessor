package org.bettingprocessor;

import java.math.BigDecimal;

public class Bet {
    private long betAmount;
    private Match match;
    private boolean won;

    public Bet(long betAmount, Match match, boolean won) {
        this.betAmount = betAmount;
        this.match = match;
        this.won = won;
    }

    public long getBetAmount() {
        return betAmount;
    }

    public long getWinAmount() {
        if (match == null) {
            return 0;
        }

        BigDecimal rate = won ? match.getRateForWinningSide() : BigDecimal.ONE;
        return (long) (betAmount * rate.doubleValue());
    }

}

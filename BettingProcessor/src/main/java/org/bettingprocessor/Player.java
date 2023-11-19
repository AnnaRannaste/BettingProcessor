package org.bettingprocessor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String playerId;
    private long balance;
    private int totalBets;
    private int wonBets;
    private List<Bet> bets;

    public Player(String playerId) {
        this.playerId = playerId;
        this.balance = 0;
        this.totalBets = 0;
        this.wonBets = 0;
        this.bets = new ArrayList<>();
    }

    public String getPlayerId() {
        return playerId;
    }

    public long getBalance() {
        return balance;
    }

    public long updateBalance(long amount) {
        balance += amount;
        return balance;
    }

    public void placeBet(long betAmount, boolean won, Match match) {
        totalBets++;
        if (won) {
            wonBets++;
            updateBalance(betAmount);
        } else {
            updateBalance(-betAmount);
        }
        Bet bet = new Bet(betAmount, match, won);
        addBet(bet);
    }

    public BigDecimal getWinRate() {
        if (totalBets == 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(wonBets).divide(BigDecimal.valueOf(totalBets), 2, BigDecimal.ROUND_HALF_UP);
    }

    public long getBalanceChangeFromBets() {

        long balanceChange = 0;
        for (Bet bet : bets) {
            long winAmount = bet.getWinAmount();
            long betAmount = bet.getBetAmount();

            balanceChange += bet.getWinAmount() + bet.getBetAmount();
        }
        return balanceChange;
    }

    public void addBet(Bet bet) {
        bets.add(bet);
    }


    private boolean hasIllegalAction = false;
    private String firstIllegalAction;

    public void flagIllegalAction(String action) {
        if (!hasIllegalAction) {
            hasIllegalAction = true;
            firstIllegalAction = action;
        }
    }

    public boolean hasIllegalAction() {
        return hasIllegalAction;
    }

    public String getFirstIllegalAction() {
        return firstIllegalAction;
    }


    public boolean isLegitimate() {
        System.out.println("Checking legitimacy for player " + playerId);
        if (hasIllegalAction) {
            System.out.println("Player " + playerId + " is illegitimate due to attempted illegal action.");
            return false;
        }


        System.out.println("Player " + playerId + " is legitimate.");
        System.out.println("--------------------------");
        return true;
    }


    public void processWithdrawal(long withdrawalAmount) {
        if (withdrawalAmount <= balance) {
            updateBalance(-withdrawalAmount);
        } else {

        }
    }

    public int getTotalBets() {
        return totalBets;
    }

    public List<Bet> getBets() {
        return bets;
    }


}

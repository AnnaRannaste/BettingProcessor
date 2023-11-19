package org.bettingprocessor;

import java.math.BigDecimal;

public class Match {
    private String matchId;
    private BigDecimal rateA;
    private BigDecimal rateB;
    private String result;

    public Match(String matchId, BigDecimal rateA, BigDecimal rateB) {
        this.matchId = matchId;
        this.rateA = rateA;
        this.rateB = rateB;
        this.result = "";
    }

    public BigDecimal getRateA() {
        return rateA;
    }

    public BigDecimal getRateB() {
        return rateB;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public BigDecimal getRateForWinningSide() {
        if (result.equals("A")) {
            return rateA;
        } else if (result.equals("B")) {
            return rateB;
        } else {

            return BigDecimal.ONE;
        }
    }
}






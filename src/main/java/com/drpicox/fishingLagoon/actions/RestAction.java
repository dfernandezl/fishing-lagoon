package com.drpicox.fishingLagoon.actions;

public class RestAction extends Action {

    public static final Action DEFAULT = new RestAction();

    @Override
    public boolean equals(Object obj) {
        return obj instanceof RestAction;
    }

    @Override
    public int hashCode() {
        return 7473;
    }

    @Override
    public String toString() {
        return "rest";
    }
}

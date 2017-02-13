package com.bmj.greader.data.model.issue;

/**
 * Created by Administrator on 2016/12/22 0022.
 */
public enum IssueState {
    open(0),
    closed(1),
    all(2);

    public int value;

    IssueState(int value) {
        this.value = value;
    }

    public static IssueState fromValue(int value) {
        switch (value) {
            case 0:
                return IssueState.open;
            case 1:
                return IssueState.closed;
            case 2:
                return IssueState.all;
            default:
                return IssueState.open;
        }
    }
}

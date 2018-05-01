package com.drpicox.fishingLagoon.common;

import java.util.UUID;

public class UuidIdGenerator implements IdGenerator {
    @Override
    public String next() {
        return UUID.randomUUID().toString();
    }
}

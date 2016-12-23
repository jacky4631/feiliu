// 
// Decompiled by Procyon v0.5.30
// 

package com.jiam365.flow.server.engine;

public class SimpleQueryFrequencyStrategy implements QueryFrequencyStrategy
{
    @Override
    public int delaySeconds(final int times) {
        if (times < 3) {
            return 15;
        }
        if (times < 5) {
            return 30;
        }
        if (times < 10) {
            return 60;
        }
        return 180;
    }
}

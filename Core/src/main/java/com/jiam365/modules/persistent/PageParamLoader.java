package com.jiam365.modules.persistent;

public abstract interface PageParamLoader<T>
{
    public abstract Page<T> parse();
}

package com.github.taurvi.valor.commands;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import lombok.Data;

import java.util.List;

@Data
public class CommandRegistry {
    private final List<ValorCommand> commandList;

    @Inject
    public CommandRegistry(Rollcall rollcall) {
        this.commandList = Lists.newArrayList();
        commandList.add(rollcall);
    }
}

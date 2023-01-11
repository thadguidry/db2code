package com.homihq.model;

import lombok.Data;

@Data
public class One2One {
    private boolean association;
    private boolean shared;
    private boolean owningSide;

    private Field referencedField;
}

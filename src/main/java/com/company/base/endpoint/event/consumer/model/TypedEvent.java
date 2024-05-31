package com.company.base.endpoint.event.consumer.model;

import com.company.base.PojaGenerated;

@PojaGenerated
public record TypedEvent(String typeName, Object payload) {}

package poja.compute.only.endpoint.event.consumer.model;

import poja.compute.only.PojaGenerated;
import poja.compute.only.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}

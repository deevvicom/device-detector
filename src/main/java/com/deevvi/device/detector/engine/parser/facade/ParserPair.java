package com.deevvi.device.detector.engine.parser.facade;

import com.deevvi.device.detector.engine.parser.Parser;
import com.google.common.base.Preconditions;

/**
 * Simple POJO class that stores together a parser and its order.
 */
public final class ParserPair {

    /**
     * Parser.
     */
    private final Parser parser;

    /**
     * Parser order.
     */
    private final int order;

    /**
     * Constructor.
     */
    public ParserPair(Parser parser, int order) {

        Preconditions.checkNotNull(parser, "Parser cannot be null.");
        Preconditions.checkArgument(order > 0, "Order cannot be negative.");

        this.parser = parser;
        this.order = order;
    }

    public Parser getParser() {

        return parser;
    }

    public int getOrder() {

        return order;
    }
}

package com.deevvi.device.detector.engine.parser.facade;

import com.deevvi.device.detector.engine.parser.Parser;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Integer.MAX_VALUE;

/**
 * Class that aggregates all client parsers.
 */
public final class ParserFacade implements Parser {

    /**
     * List with parsers, along with their priority.
     */
    private final List<ParserPair> parsers;

    /**
     * Internal thread pool, for async execution.
     */
    private final ExecutorService service;

    /**
     * Constructor.
     */
    public ParserFacade(List<ParserPair> parsers) {

        Preconditions.checkArgument(parsers != null && !parsers.isEmpty(),
                "List of client parsers cannot be empty.");

        this.parsers = parsers;
        this.service = Executors.newFixedThreadPool(parsers.size());
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        List<Future<ParserResult>> futures = Lists.newArrayList();
        parsers.forEach(parser -> futures.add(service.submit(() ->
                new ParserResult(parser.getOrder(), parser.getParser().parse(userAgent)))));

        AtomicBoolean done = new AtomicBoolean(false);
        AtomicReference<Map<String, String>> result = new AtomicReference<>(Maps.newHashMap());
        AtomicInteger minPosition = new AtomicInteger(MAX_VALUE);
        while (!done.get()) {
            done.set(true);
            futures.forEach(future -> {
                if (future.isDone()) {
                    try {
                        processFuture(future, result, minPosition);
                    } catch (Exception e) {
                    }
                } else {
                    done.set(false);
                }
            });
            Thread.yield();
        }

        return result.get();
    }

    private void processFuture(Future<ParserResult> future, AtomicReference<Map<String, String>> result, AtomicInteger minPosition) throws Exception {
        ParserResult parserResult = future.get();
        if (!parserResult.getResult().isEmpty()) {
            if (parserResult.getOrder() < minPosition.get()) {
                minPosition.set(parserResult.getOrder());
                result.set(parserResult.getResult());
            } else if (result.get().containsKey(DEVICE_TYPE)) {
                if (result.get().get(DEVICE_TYPE).equals(parserResult.getResult().get(DEVICE_TYPE))) {
                    result.set(processResult(result.get(), parserResult));
                }

            }
        }
    }

    private Map<String, String> processResult(Map<String, String> result, ParserResult parserResult) {
        Map<String, String> map = Maps.newHashMap(result);
        if (parserResult.getResult().size() > result.size()) {
            return parserResult.getResult();
        }
        return map;
    }

    private static class ParserResult {

        private final int order;
        private final Map<String, String> result;

        public ParserResult(int order, Map<String, String> result) {

            this.order = order;
            this.result = result;
        }

        public int getOrder() {

            return order;
        }

        public Map<String, String> getResult() {

            return result;
        }
    }
}

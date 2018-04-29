package com.drpicox.fishingLagoon.parser;

import java.util.*;

public class RoundParser {

    private PropsParser propsParser;

    public RoundParser(PropsParser propsParser) {
        this.propsParser = propsParser;
    }

    public RoundDescriptor parse(String tournamentText) {
        Props props = propsParser.parse(tournamentText);
        State state = new State(props);

        return state.getRound();
    }

    private static class State {
        final Props props;
        final Map<String, LagoonDescriptor> lagoonDescriptorMap = new HashMap<>();

        public State(Props props) {
            this.props = props;
        }

        public RoundDescriptor getRound() {
            Double maxDensity = props.getDouble("maxDensity", 5.0);
            Integer weekCount = props.getInteger("weekCount", 10);
            String[] lagoonNames = props.getCsv("lagoons", "");

            Long seatMilliseconds = props.getLong("seatMilliseconds", 20000L);
            Long commandMilliseconds = props.getLong("commandMilliseconds", 20000L);
            Long scoreMilliseconds = props.getLong("scoreMilliseconds", 20000L);

            if (maxDensity == null || lagoonNames == null || weekCount == null) return null;

            List<LagoonDescriptor> lagoonDescriptors = getLagoonDescriptors(lagoonNames);
            return new RoundDescriptor(seatMilliseconds, commandMilliseconds, scoreMilliseconds, maxDensity, lagoonDescriptors, weekCount);
        }

        private List<LagoonDescriptor> getLagoonDescriptors(String... lagoonNames) {
            List<LagoonDescriptor> lagoonDescriptors = new ArrayList<>();
            for (String lagoonName : lagoonNames) {
                LagoonDescriptor lagoonDescriptor = getLagoonDescriptor(lagoonName);
                lagoonDescriptors.add(lagoonDescriptor);
            }
            return lagoonDescriptors;
        }

        private LagoonDescriptor getLagoonDescriptor(String key) {
            LagoonDescriptor lagoonDescriptor = lagoonDescriptorMap.get(key);
            if (lagoonDescriptor != null) return lagoonDescriptor;

            Long fishPopulation = props.getLong(key + ".fishPopulation", 0L);
            if (fishPopulation != null) {
                lagoonDescriptor = new LagoonDescriptor(fishPopulation);
                lagoonDescriptorMap.put(key, lagoonDescriptor);
            }

            return lagoonDescriptor;
        }
    }
}

package com.deevvi.device.detector.engine.parser;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.engine.utils.Tuple;
import com.deevvi.device.detector.model.OperatingSystem;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.deevvi.device.detector.engine.utils.ConfigUtils.fetchMapFromFile;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Parser to determine the operating system.
 */
public final class OperatingSystemParser implements Parser, ListLoader<OperatingSystem> {

    /**
     * List with operating systems having name not capitalized.
     */
    private static final Set<String> notCapitalizedOS = ImmutableSet.of("", "iOS", "webOS", "palmOS");

    /**
     * Regexes for OS platforms.
     */
    private static final Map<String, Pattern> platformMapping = new ImmutableMap.Builder<String, Pattern>()
            .put("ARM", Pattern.compile("(?:^|[^A-Z0-9\\-_]|[^A-Z0-9\\-]_|sprd-)(?:arm)", CASE_INSENSITIVE))
            .put("x64", Pattern.compile("(?:^|[^A-Z0-9\\-_]|[^A-Z0-9\\-]_|sprd-)(?:WOW64|x64|win64|amd64|x86_64)", CASE_INSENSITIVE))
            .put("x86", Pattern.compile("(?:^|[^A-Z0-9\\-_]|[^A-Z0-9\\-]_|sprd-)(?:i[0-9]86|i86pc)", CASE_INSENSITIVE))
            .build();

    /**
     * Mapping from operating systems names to short names.
     */
    private static final Map<String, String> operatingSystemsNames = fetchMapFromFile("/configs/availableOperatingSystems");

    /**
     * Mapping from operating systems families
     */
    private static final Map<String, String> operatingSystemsFamilies = fetchMapFromFile("/configs/operatingSystemsFamilies");

    /**
     * List with operating systems models.
     */
    private final List<OperatingSystem> operatingSystems = streamToList();

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public Map<String, String> parse(String userAgent) {
        return operatingSystems
                .stream()
                .map(os -> new Tuple<>(os, os.getPattern().matcher(userAgent)))
                .filter(t -> t.getMatcher().find())
                .findFirst()
                .map(t -> buildResult(userAgent, t))
                .orElse(Maps.newHashMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OperatingSystem toObject(Object rawObject) {
        Map<String, String> map = (Map) rawObject;
        return new OperatingSystem.Builder()
                .withPattern(toPattern(map.get(REGEX)))
                .withName(map.get(NAME))
                .withVersion(map.get(VERSION))
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/operating-systems.yml";
    }

    private Map<String, String> buildResult(String userAgent, Tuple<OperatingSystem> t) {
        Map<String, String> map = Maps.newHashMap();
        Optional<String> name = buildName(buildVersion(t.getMatcher(), t.get().getName()));
        name.ifPresent(val -> {
            map.put(NAME, val);
            if (operatingSystemsNames.containsKey(val)) {
                map.put(SHORT_NAME, operatingSystemsNames.get(val));
            }

            map.put(OS_FAMILY, operatingSystemsFamilies.getOrDefault(operatingSystemsNames.get(val), UNKNOWN));
        });
        Optional<String> version = buildVersion(t.getMatcher(), t.get().getVersion()).map(Parser::clear);
        version.ifPresent(val -> map.put(VERSION, val));
        map.put(PLATFORM, buildPlatform(userAgent));

        return map;
    }

    private Optional<String> buildName(Optional<String> name) {

        if (name.isPresent() && notCapitalizedOS.contains(StringUtils.trimToEmpty(name.get()))) {
            return name;
        }

        return name.map(this::capitalize);
    }


    private String buildPlatform(String userAgent) {

        for (Map.Entry<String, Pattern> entry : platformMapping.entrySet()) {

            Matcher matcher = entry.getValue().matcher(userAgent);
            if (matcher.find()) {
                return entry.getKey();
            }
        }

        return EMPTY_STRING;
    }
}

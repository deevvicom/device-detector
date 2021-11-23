package com.deevvi.device.detector.engine.parser.client;

import com.deevvi.device.detector.engine.loader.ListLoader;
import com.deevvi.device.detector.engine.parser.Parser;
import com.deevvi.device.detector.engine.utils.Tuple;
import com.deevvi.device.detector.model.client.Browser;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.deevvi.device.detector.engine.utils.ConfigUtils.fetchMapFromFile;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Parser to validate if input is a valid browser.
 */
public final class BrowserParser implements Parser, ListLoader<Browser> {

    /**
     * String constants.
     */
    private static final String GECKO_ENGINE = "Gecko";

    /**
     * Patterns.
     */
    private static final String ENGINE_VERSION_REGEX = "\\s*/?\\s*((?:((?=\\d+\\.\\d)\\d+[.\\d]*)|\\d{1,7}(?=(?:\\D|$))))";
    private static final Pattern GECKO_VERSION_PATTERN = Pattern.compile("[ ](?:rv[: ]([0-9\\.]+)).*gecko/", CASE_INSENSITIVE);

    /**
     * Static list of browsers.
     */
    private static final Map<String, String> browsersMapping = fetchMapFromFile("/configs/availableBrowsers.txt");

    /**
     * Static list of browser families.
     */
    private static final Map<String, String> browsersFamilies = fetchMapFromFile("/configs/browserFamilies.txt");

    /**
     * List of browsers models.
     */
    private final List<Browser> browsers = streamToList();

    /**
     * List of browsers engines parsers.
     */
    private final BrowserEngineParser browserEngineParser;

    /**
     * Constructor.
     */
    public BrowserParser(BrowserEngineParser browserEngineParser) {

        Preconditions.checkNotNull(browserEngineParser, "Browser engine parser cannot be null.");

        this.browserEngineParser = browserEngineParser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> parse(String userAgent) {

        return browsers.stream()
                .map(browser -> new Tuple<>(browser, browser.getPattern().matcher(userAgent)))
                .filter(t -> t.getMatcher().find())
                .findFirst()
                .map(t -> {
                    Optional<String> name = buildByMatcher(t.getMatcher(), t.get().getName());
                    Optional<String> version = buildVersion(t.getMatcher(), t.get().getVersion());
                    Optional<String> engine = buildEngine(t.get(), userAgent, version.orElse(EMPTY_STRING));
                    Optional<String> engineVersion = engine.isPresent() ? getEngineVersion(engine.get(), userAgent) : Optional.empty();

                    return toMap(name, version, engine, engineVersion);
                }).orElse(Maps.newHashMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Browser toObject(Object rawObject) {

        Map<String, Object> map = (Map) rawObject;
        Map<String, String> engineMap = Maps.newHashMap();
        if (map.containsKey(ENGINE)) {
            Map engMap = ((Map) map.get(ENGINE));
            if (engMap.containsKey(DEFAULT)) {
                engineMap.put(DEFAULT, String.valueOf(engMap.get(DEFAULT)));
            }

            if (engMap.containsKey(VERSIONS)) {
                Map versionMap = (Map) engMap.get(VERSIONS);
                for (Object obj : versionMap.entrySet()) {
                    Map.Entry entry = (Map.Entry) obj;
                    engineMap.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                }
            }
        }

        return new Browser.Builder()
                .withName(String.valueOf(map.get(NAME)))
                .withRawRegex((String) map.get(REGEX))
                .withVersion(String.valueOf(map.get(VERSION)))
                .withEngine(engineMap)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilePath() {
        return "/regexes/client/browsers.yml";
    }

    private Optional<String> buildEngine(Browser browser, String userAgent, String stringVersion) {

        AtomicReference<String> engine = new AtomicReference<>(browser.getEngines().getOrDefault(DEFAULT, EMPTY_STRING));
        if (StringUtils.isNotBlank(stringVersion)) {
            DefaultArtifactVersion defaultVersion = new DefaultArtifactVersion(stringVersion);
            AtomicReference<DefaultArtifactVersion> versionToReturn = new AtomicReference<>(new DefaultArtifactVersion("0"));

            browser.getEngines().forEach((key, value) -> {
                if (!key.equals(DEFAULT)) {
                    DefaultArtifactVersion version = new DefaultArtifactVersion(key);
                    if ((defaultVersion.compareTo(version) >= 0) &&
                            (version.compareTo(versionToReturn.get()) >= 0)) {
                        engine.set(value);
                        versionToReturn.set(new DefaultArtifactVersion(key));
                    }
                }
            });
        }

        if (StringUtils.isEmpty(engine.get())) {
            Map<String, String> result = browserEngineParser.parse(userAgent);
            if (result.containsKey(ENGINE_NAME)) {
                engine.set(result.get(ENGINE_NAME));
            }
        }

        return Optional.of(engine.get());
    }

    private static Optional<String> getEngineVersion(String engine, String userAgent) {

        if (engine.equals(GECKO_ENGINE)) {
            Matcher matcher = GECKO_VERSION_PATTERN.matcher(userAgent);
            if (matcher.find()) {
                return Optional.of(matcher.group(1));
            }

            return Optional.empty();
        }

        Pattern pattern = Pattern.compile(engine + ENGINE_VERSION_REGEX, CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(userAgent);

        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        }

        return Optional.empty();
    }

    private Map<String, String> toMap(Optional<String> name, Optional<String> version, Optional<String> engine, Optional<String> engineVersion) {

        Map<String, String> map = Maps.newHashMap();
        map.put(DEVICE_TYPE, BROWSER);
        name.ifPresent(val -> map.put(NAME, val));
        version.ifPresent(val -> map.put(VERSION, val));
        engine.ifPresent(val -> map.put(ENGINE, val));
        engineVersion.ifPresent(val -> map.put(ENGINE_VERSION, val));

        if (name.isPresent() && browsersMapping.containsKey(name.get())) {
            map.put(SHORT_NAME, browsersMapping.get(name.get()));
            if (browsersFamilies.containsKey(browsersMapping.get(name.get()))) {
                map.put(BROWSER_FAMILY, browsersFamilies.getOrDefault(browsersMapping.get(name.get()), EMPTY_STRING));
            } else {
                map.put(BROWSER_FAMILY, UNKNOWN);
            }
        }

        return map;
    }
}

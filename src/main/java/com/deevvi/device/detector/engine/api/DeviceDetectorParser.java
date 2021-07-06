package com.deevvi.device.detector.engine.api;

import com.deevvi.device.detector.engine.parser.BotParser;
import com.deevvi.device.detector.engine.parser.OperatingSystemParser;
import com.deevvi.device.detector.engine.parser.VendorFragmentsParser;
import com.deevvi.device.detector.engine.parser.client.BrowserEngineParser;
import com.deevvi.device.detector.engine.parser.client.BrowserParser;
import com.deevvi.device.detector.engine.parser.client.FeedReaderParser;
import com.deevvi.device.detector.engine.parser.client.LibraryParser;
import com.deevvi.device.detector.engine.parser.client.MediaPlayerParser;
import com.deevvi.device.detector.engine.parser.client.MobileAppParser;
import com.deevvi.device.detector.engine.parser.client.PIMDeviceParser;
import com.deevvi.device.detector.engine.parser.device.CameraParser;
import com.deevvi.device.detector.engine.parser.device.CarParser;
import com.deevvi.device.detector.engine.parser.device.ConsoleParser;
import com.deevvi.device.detector.engine.parser.device.MobileParser;
import com.deevvi.device.detector.engine.parser.device.NotebookParser;
import com.deevvi.device.detector.engine.parser.device.PortableMediaPlayerParser;
import com.deevvi.device.detector.engine.parser.device.ShellTvParser;
import com.deevvi.device.detector.engine.parser.device.TelevisionParser;
import com.deevvi.device.detector.engine.parser.facade.ParserFacade;
import com.deevvi.device.detector.engine.parser.facade.ParserPair;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.deevvi.device.detector.engine.parser.Parser.BRAND;
import static com.deevvi.device.detector.engine.parser.Parser.DEVICE_TYPE;
import static com.deevvi.device.detector.engine.parser.Parser.EMPTY_STRING;
import static com.deevvi.device.detector.engine.parser.Parser.NAME;
import static com.deevvi.device.detector.engine.parser.Parser.OS_FAMILY;
import static com.deevvi.device.detector.engine.parser.Parser.SHORT_NAME;
import static com.deevvi.device.detector.engine.parser.Parser.VENDOR;
import static com.deevvi.device.detector.engine.parser.Parser.VERSION;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.DESKTOP;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.FEATURE_PHONE;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.SMARTPHONE;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.TABLET;
import static com.deevvi.device.detector.engine.parser.device.DeviceType.TV;
import static com.deevvi.device.detector.engine.utils.ConfigUtils.fetchListFromFile;
import static java.util.regex.Pattern.CASE_INSENSITIVE;

/**
 * Collator over all parser to aggregate the details for an user agent.
 */
public final class DeviceDetectorParser {

    /**
     * Collections with configs loaded from external files.
     */
    private static final List<String> DESKTOP_OS_LIST = fetchListFromFile("/configs/desktopOperatingSystems");
    private static final List<String> APPLE_OS_LIST = fetchListFromFile("/configs/appleOperatingSystems");
    private static final List<String> TV_BROWSER_LIST = fetchListFromFile("/configs/tvBrowserList");

    /**
     * Patterns.
     */
    private static final Pattern MOBILE_PATTERN = Pattern.compile("(?:^|[^A-Z_-])(?:Android( [\\.0-9]+)?; Mobile;)", CASE_INSENSITIVE);
    private static final Pattern TOUCH_PATTERN = Pattern.compile("(?:^|[^A-Z_-])(?:Touch)", CASE_INSENSITIVE);
    private static final Pattern ANDROID_PATTERN = Pattern.compile("(?:^|[^A-Z_-])(?:Android( [\\.0-9]+)?; Tablet;)", CASE_INSENSITIVE);
    private static final Pattern OPERA_PATTERN = Pattern.compile("(?:^|[^A-Z_-])(?:Opera Tablet)", CASE_INSENSITIVE);
    private static final Pattern CHROME_SMARTPHONE_PATTERN = Pattern.compile("(?:^|[^A-Z_-])Chrome/[\\.0-9]* (?:Mobile|eliboM)", CASE_INSENSITIVE);
    private static final Pattern CHROME_TABLET_PATTERN = Pattern.compile("(?:^|[^A-Z_-])(?:Chrome/[\\.0-9]* (?!Mobile))", CASE_INSENSITIVE);
    private static final Pattern CHROME_PATTERN = Pattern.compile("(?:^|[^A-Z_-])(?:Chrome/[\\.0-9]*)", CASE_INSENSITIVE);
    private static final Pattern SMART_TV_OR_TIZIEN_TV = Pattern.compile("(?:^|[^A-Z_-])(SmartTV|Tizen.+ TV .+$)", CASE_INSENSITIVE);
    private static final Pattern OPERA_TV_STORE = Pattern.compile("(?:^|[^A-Z_-])(?:Opera TV Store)", CASE_INSENSITIVE);
    private static final Pattern DESKTOP_FRAGMENT = Pattern.compile("(?:^|[^A-Z_-])(?:Desktop (x(?:32|64)|WOW64);)", CASE_INSENSITIVE);

    /**
     * Bot parser.
     */
    private final BotParser botParser;

    /**
     * Operating system parser.
     */
    private final OperatingSystemParser operatingSystemParser;

    /**
     * Vendor fragment parser.
     */
    private final VendorFragmentsParser vendorFragmentsParser;

    /**
     * Facade for parsing device.
     */
    private final ParserFacade deviceParser;

    /**
     * Facade for parsing client.
     */
    private final ParserFacade clientParser;

    /**
     * Constructor.
     */
    private DeviceDetectorParser() {
        botParser = new BotParser();
        operatingSystemParser = new OperatingSystemParser();
        vendorFragmentsParser = new VendorFragmentsParser();

        List<ParserPair> parsers = Lists.newArrayList();
        parsers.add(new ParserPair(new TelevisionParser(), 1));
        parsers.add(new ParserPair(new ShellTvParser(), 2));
        parsers.add(new ParserPair(new NotebookParser(), 3));
        parsers.add(new ParserPair(new ConsoleParser(), 4));
        parsers.add(new ParserPair(new CarParser(), 5));
        parsers.add(new ParserPair(new CameraParser(), 6));
        parsers.add(new ParserPair(new PortableMediaPlayerParser(), 7));
        parsers.add(new ParserPair(new MobileParser(), 8));
        deviceParser = new ParserFacade(parsers);

        List<ParserPair> clientsList = Lists.newArrayList();
        clientsList.add(new ParserPair(new FeedReaderParser(), 1));
        clientsList.add(new ParserPair(new MobileAppParser(), 2));
        clientsList.add(new ParserPair(new MediaPlayerParser(), 3));
        clientsList.add(new ParserPair(new PIMDeviceParser(), 4));
        clientsList.add(new ParserPair(new BrowserParser(new BrowserEngineParser()), 5));
        clientsList.add(new ParserPair(new LibraryParser(), 6));
        clientParser = new ParserFacade(clientsList);
    }

    /**
     * Factory method.
     *
     * @return device parser
     */
    public static DeviceDetectorParser getClient() {

        return new DeviceDetectorParser();
    }

    /**
     * Parse the user agent.
     *
     * @param userAgent the user agent.
     * @return result of parsing the user agent
     */
    public DeviceDetectorResult parse(String userAgent) {

        if (StringUtils.isEmpty(userAgent)) {

            return DeviceDetectorResult.fromEmptyResult();
        }

        Map<String, String> botMap = botParser.parse(userAgent);
        if (!botMap.isEmpty()) {

            return DeviceDetectorResult.fromBotResult(botMap);
        }

        return tryDetectDevice(userAgent);
    }

    private DeviceDetectorResult tryDetectDevice(String userAgent) {

        Map<String, String> osDetails = operatingSystemParser.parse(userAgent);
        Map<String, String> clientDetails = clientParser.parse(userAgent);
        Map<String, String> deviceDetails = deviceParser.parse(userAgent);

        if (noBrandDetected(deviceDetails)) {
            Map<String, String> vendorResults = vendorFragmentsParser.parse(userAgent);
            String vendor = null;
            if (!vendorResults.isEmpty()) {
                vendor = vendorResults.get(VENDOR);
            }
            if (StringUtils.isNotBlank(vendor)) {
                deviceDetails.put(BRAND, vendor);
            } else if (isRunningMacOS(osDetails)) {
                deviceDetails.put(BRAND, "Apple");
            }
        }

        if (noDeviceTypeDetected(deviceDetails)) {
            if (isAndroid(osDetails) && hasChromePattern(userAgent)) {
                if (isChromeSmartphone(userAgent)) {
                    deviceDetails.put(DEVICE_TYPE, SMARTPHONE.getDeviceName());
                } else if (isChromeTablet(userAgent)) {
                    deviceDetails.put(DEVICE_TYPE, TABLET.getDeviceName());
                }
            } else if ((hasAndroidTableFragment(userAgent) || hasOperaTabletFragment(userAgent))) {
                deviceDetails.put(DEVICE_TYPE, TABLET.getDeviceName());
            } else if (hasAndroidMobileFragment(userAgent)) {
                deviceDetails.put(DEVICE_TYPE, SMARTPHONE.getDeviceName());
            }
        }

        if (noDeviceTypeDetected(deviceDetails) && isAndroid(osDetails) && osDetails.containsKey(VERSION)) {
            String stringVersion = osDetails.get(VERSION);
            DefaultArtifactVersion defaultVersion = new DefaultArtifactVersion(stringVersion);
            if (defaultVersion.compareTo(new DefaultArtifactVersion("2.0")) < 0) {
                deviceDetails.put(DEVICE_TYPE, SMARTPHONE.getDeviceName());
            } else if (defaultVersion.compareTo(new DefaultArtifactVersion("3.0")) >= 0
                    && defaultVersion.compareTo(new DefaultArtifactVersion("4.0")) < 0) {
                deviceDetails.put(DEVICE_TYPE, TABLET.getDeviceName());
            }
        }

        if (isFeaturePhone(deviceDetails) && isAndroid(osDetails)) {
            deviceDetails.put(DEVICE_TYPE, SMARTPHONE.getDeviceName());
        }
        String osName = osDetails.getOrDefault(NAME, EMPTY_STRING);
        if (noDeviceTypeDetected(deviceDetails) && (osName.equals("Windows RT") || (osName.equals("Windows")
                && new DefaultArtifactVersion(osDetails.getOrDefault(VERSION, "0")).compareTo(new DefaultArtifactVersion("8.0")) >= 0
                && isTouch(userAgent)))) {
            deviceDetails.put(DEVICE_TYPE, TABLET.getDeviceName());
        }
        if (isOperaTV(userAgent)) {
            deviceDetails.put(DEVICE_TYPE, TV.getDeviceName());
        }
        if (noDeviceTypeDetected(deviceDetails) && isTizienOrSmartTv(userAgent)) {
            deviceDetails.put(DEVICE_TYPE, TV.getDeviceName());
        }
        if (noDeviceTypeDetected(deviceDetails) && isTVBrowser(clientDetails)) {
            deviceDetails.put(DEVICE_TYPE, TV.getDeviceName());
        }
        if (!isDesktopDeviceType(deviceDetails) && userAgent.toLowerCase().contains("desktop") && hasDesktopFragment(userAgent)) {
            deviceDetails.put(DEVICE_TYPE, DESKTOP.getDeviceName());
        }
        if (noDeviceTypeDetected(deviceDetails) && isDesktop(osDetails)) {
            deviceDetails.put(DEVICE_TYPE, DESKTOP.getDeviceName());
        }

        return DeviceDetectorResult.fromDevice(osDetails, clientDetails, deviceDetails);
    }

    private boolean hasDesktopFragment(String userAgent) {
        return DESKTOP_FRAGMENT.matcher(userAgent).find();
    }

    private boolean isDesktopDeviceType(Map<String, String> deviceDetails) {
        return deviceDetails.getOrDefault(DEVICE_TYPE, EMPTY_STRING).equals(DESKTOP.getDeviceName());
    }

    private boolean isTVBrowser(Map<String, String> clientDetails) {
        return TV_BROWSER_LIST.contains(clientDetails.getOrDefault(NAME, EMPTY_STRING));
    }

    private boolean isOperaTV(String userAgent) {
        return OPERA_TV_STORE.matcher(userAgent).find();
    }

    private boolean hasChromePattern(String userAgent) {
        return CHROME_PATTERN.matcher(userAgent).find();
    }

    private boolean isChromeTablet(String userAgent) {
        return CHROME_TABLET_PATTERN.matcher(userAgent).find();
    }

    private boolean isChromeSmartphone(String userAgent) {
        return CHROME_SMARTPHONE_PATTERN.matcher(userAgent).find();
    }

    private boolean noDeviceTypeDetected(Map<String, String> deviceDetails) {
        return StringUtils.isEmpty(deviceDetails.getOrDefault(DEVICE_TYPE, EMPTY_STRING));
    }

    private boolean noBrandDetected(Map<String, String> deviceDetails) {
        return StringUtils.isEmpty(deviceDetails.getOrDefault(BRAND, EMPTY_STRING));
    }

    private boolean isFeaturePhone(Map<String, String> deviceDetails) {
        return deviceDetails.getOrDefault(DEVICE_TYPE, EMPTY_STRING).equals(FEATURE_PHONE.getDeviceName());
    }

    private boolean isTizienOrSmartTv(String userAgent) {

        return SMART_TV_OR_TIZIEN_TV.matcher(userAgent).find();
    }

    private boolean isRunningMacOS(Map<String, String> osDetails) {

        String osShortName = osDetails.get(SHORT_NAME);
        return StringUtils.isNotEmpty(osShortName) && APPLE_OS_LIST.contains(osShortName);
    }

    private boolean hasOperaTabletFragment(String userAgent) {

        return OPERA_PATTERN.matcher(userAgent).find();
    }

    private boolean hasAndroidTableFragment(String userAgent) {

        return ANDROID_PATTERN.matcher(userAgent).find();
    }

    private boolean isTouch(String userAgent) {

        return TOUCH_PATTERN.matcher(userAgent).find();
    }

    private boolean isAndroid(Map<String, String> osDetails) {

        String osFamily = osDetails.get(OS_FAMILY);
        return StringUtils.isNotBlank(osFamily) && osFamily.equals("Android");
    }

    private boolean hasAndroidMobileFragment(String userAgent) {

        return MOBILE_PATTERN.matcher(userAgent).find();
    }

    private boolean isDesktop(Map<String, String> osDetails) {

        if (osDetails.isEmpty()) {
            return false;
        }

        String osFamily = osDetails.get(OS_FAMILY);
        return StringUtils.isNotEmpty(osFamily) && DESKTOP_OS_LIST.contains(osFamily);
    }
}

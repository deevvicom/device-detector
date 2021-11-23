## Device Detector
The aim of this library is to fetch as much as possible information from a user-agent string.
If a valid user-agent is provided, then the following information is determined:
* Bot or not
* For non-bots:
  * browser or application
  * operating system
  * device type
  * device brand  

### Features
* Over 200 types of bots
* Almost 50 mobile applications
* Almost 1000 brands
* Almost 100 types of operating systems
* Around 200 types of browsers
* On average, it takes *12ms* to parse a user-agent

### How to use it
The recommended way to use this library is through your build tool.

The device-detector artifact is published to Maven Central, using the group `com.deevvi`.

Latest stable version is `1.11.0`.

Therefore,it can be added to your Gradle project by adding the dependencies:

```
compile "com.deevvi:device-detector:1.11.0"
```
and in Maven:
```
<dependency>
    <groupId>com.deevvi</groupId>
    <artifactId>device-detector</artifactId>
    <version>1.11.0</version>
</dependency>
```

Code example:

```
DeviceDetectorParser parser = DeviceDetectorParser.getClient();
DeviceDetectorResult result = parser.parse("Mozilla/5.0 (Linux; Android 7.1.1; G8232 Build/41.2.A.0.235) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.91 Mobile Safari/537.36");

System.out.println("Result found: " + result.found());
System.out.println("User-agent is mobile: " + result.isMobileDevice());
System.out.println("User-agent is bot: " + result.isBot());
System.out.println("Result as JSON: " + result.toJSON());
```

Output:
```
Result found: true
User-agent is mobile: true
User-agent is bot: false
Result as JSON: {"os":{"osFamily":"Android","name":"Android","shortName":"AND","version":"7.1.1","platform":""},"client":{"deviceType":"browser","engine":"Blink","browserFamily":"Chrome","name":"Chrome Mobile","shortName":"CM","version":"55.0.2883.91"},"device":{"deviceType":"smartphone","model":"Xperia XZs","brand":"Sony"}}
```
# Overview
java-firebase-fcm-client is push notification client for firebase cloud messaging API.
- You can use on your server to send downstream message from firebase.
- Whether the mobile(like Android) application is foreground or background, you can handle push notifications in the same way, without showing notification in the notification tray.

It is licensed under [MIT license](https://opensource.org/licenses/MIT).

# Quick start
## Example Server->Mobile entity(mobile devices,browser front-end apps) (like Android)
- Push notification to Android Device from your server
- You can write like this code on your server.

```java
package com.example;

import org.riversun.fcm.FcmClient;
import org.riversun.fcm.model.EntityMessage;
import org.riversun.fcm.model.FcmResponse;

public class SendMessageExample1 {
	public static void main(String[] args) {

		FcmClient client = new FcmClient();
		// You can get from firebase console.
		// "select your project>project settings>cloud messaging"
		client.setAPIKey("[YOUR_SERVER_API_KEY_HERE]");

		// Data model for sending messages to specific entity(mobile devices,browser front-end apps)s
		EntityMessage msg = new EntityMessage();

		// Set registration token that can be retrieved
		// from Android entity(mobile devices,browser front-end apps) when calling
		// FirebaseInstanceId.getInstance().getToken();
		msg.addRegistrationToken("REGISTRATION_TOKEN_FOR_AN_ANDROID_DEVICE_HERE");

		// Add key value pair into payload
		msg.putStringData("myKey1", "myValue1");
		msg.putStringData("myKey2", "myValue2");

		// push
		FcmResponse res = client.pushToEntities(msg);

		System.out.println(res);

	}
}
```
### EntityMessage.java simply wraps following JSON.

```json
{ "data":{
    "myKey1":"myValue1",
    "myKey2":"myValue2"
  },
  "registration_ids":["your_registration_token1","your_registration_token2]
}
```



## Example Receive push notification on Android
Whether the application is foreground or background, you can handle push notifications in the same way.

### Example for Android is here.
https://github.com/riversun/android-firebase-fcm-client

## Example for Android overview
### - MyFirebaseMessagingService.java

```java
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String value1 = data.get("myKey1");
        String value2 = data.get("myKey2");
    }
}
```

### - MyFirebaseInstanceIDService.java

```java
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        //registration token
        final String registrationToken = FirebaseInstanceId.getInstance().getToken();

        //TODO
        //Send to the server to register and identify the entity(mobile devices,browser front-end apps) in order to send a push notification to this entity(mobile devices,browser front-end apps).

    }
}
```

### - Add services to your AndroidManifest.xml

```xml
<manifest>
  <application>
  ...

  <service android:name=".MyFirebaseMessagingService">
    <intent-filter>
      <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
  </service>
  <service android:name=".MyFirebaseInstanceIDService">
    <intent-filter>
      <action android:name="com.google.firebase.INSTANCE_ID_EVENT" />
    </intent-filter>
  </service>
  ...
  </appliation>
</manifest>
```

### - [project]/app/build.gradle

Add this into dependencies
```
 compile 'com.google.firebase:firebase-core:10.0.1'
 compile 'com.google.firebase:firebase-messaging:10.0.1'
```

Add this, it is important to place this at the bottom line.
```
apply plugin: 'com.google.gms.google-services'
```

### - [project]/build.gradle

Add this into dependencies
```
 classpath 'com.google.gms:google-services:3.0.0'
```

### if you want to check if google play service is available on the entity(mobile devices,browser front-end apps).Write like this.

```java

        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (resultCode == ConnectionResult.SUCCESS) {

            } else {
                apiAvailability.getErrorDialog(this, resultCode, 1).show();
            }
        }
```
# Links
## Firebase Console

https://console.firebase.google.com/

## Format

https://firebase.google.com/docs/cloud-messaging/http-server-ref
https://firebase.google.com/docs/cloud-messaging/send-message

## Example code for Android (receiving message)
https://github.com/riversun/android-firebase-fcm-client

# Downloads
## maven
- You can add dependencies to maven pom.xml file.
```xml
<dependency>
  <groupId>org.riversun</groupId>
  <artifactId>fcm</artifactId>
  <version>0.2.0</version>
</dependency>
```

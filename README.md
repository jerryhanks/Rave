# Rave
Rave is an android plugin for interacting with the Flutterwave payment api. It's easy to use and integrate into your existing project. Also, it takes away the extra UI design work, Rave has a nice custom dialog box (both portrait & landscape) for collecting transaction details from users.

## Design
You can view the screenshots for the demo app [Here](/screenshots) 

## Dependencies

Rave has the following dependencies

    com.android.support.appcompat-v7
    commons-codec:commons-codec
    com.squareup.okhttp3:logging-interceptor
    com.google.guava:guava
    com.squareup.retrofit2:retrofit
    com.squareup.retrofit2:converter-gson


Note : You don't have to add these dependencies yourself, they've already been added to gradle within the raveModule.


## Installation


To import Rave into your Android studio project, proceed as follows:

    1. Download project from github
    2. Unzip the downloaded file (a directory Rave master will be created)
    3. Click File > New > Import Module.
    4. In the Source directory box, Navigate to Rave master directory created earlier 
    5. Select the `rave` folder
    6. Click Finish. Once the module is imported, it appears in the Project window on the left.
    7. add `compile project(":raveModule")` to your dependencies in build.gradle at app level.
    8. Finally, clean and rebuild project.

##Alternative
Rave Dialog is currently available on Sonatype, you can grab the Snapshot by adding the following to your app level build.gradle

```
repositories {
    mavenCentral()
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots"
    }

}

```

##Initialise the Rave Dialog SDK with Context and Environment as follows:
The default environment for the Rave SDk is Testing, at any time you want to move to production, simply add the following lines of code to your Application class.
```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Rave.initEnvironment(this,Rave.ENV_TESTING);
    }
}
    
```

Note: The static method Rave.initEnvironment can take :
* Rave.ENV_PRODUCTION
* Rave.ENV_TESTING

depending on what you want. The default is Rave.ENV_TESTING.
Then include the snapshot in your dependencies as follows:

```
dependencies {
    
    ...
    
    compile 'me.jerryhanks.rave:rave-dialog:0.0.2-SNAPSHOT'
    ....
}

```

## How to use

You can call Rave within your activity as shown below with or without the item price.
However, if you call rave with the item price, the Input field for Amount will be hidden, otherwise, it will be shown.
```
public class MainActivity extends AppCompatActivity {

    private Button raveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        raveButton = (Button)findViewById(R.id.rave_btn);

        raveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create rave data object
                // All Constructor parameters must be provided.
                RaveData raveData = new RaveData.Builder(
                        "Shawarma and Coke", // item name
                        "Shawarma and Coke for kenny", // item description
                        1400.00, // item price or 0.0 if you want the user to input the amount
                        "FLWPUBK-XXXXX", // public key
                        "FLWSECK-XXXXX", // secret key
                        "test@gmail.com", // buyer's email address
                        "FLW-TXREF-XXXXX", // tx-ref
                         RaveAuthModel, //The AuthModel to use for the Current trasaction.
                        )
                        .build();

                // initialize RaveDialog using RaveData object created above and call show() to display dialog.
                RaveDialog rave = new RaveDialog(MainActivity.this, raveData);
                rave.show();
            }
        });
    }
}
```

You can also specify custom values for RaveData optional parameters as shown below
```
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.coke);

                // create rave data object
                // All Constructor parameters must be provided.
                RaveData raveData = new RaveData.Builder(
                        "Shawarma and Coke", // item name
                        "Shawarma and Coke for kenny", // item description
                        1400.00, // item price
                        "FLWPUBK-XXXXX", // public key
                        "FLWSECK-XXXXX", // secret key
                        "test@gmail.com", // buyer's email address
                        "FLW-TXREF-XXXXX" // tx-ref)

                        // optional parameters
                        .withItemImage(bitmap)
                        .withCountry("Nigeria")
                        .withCurrency("NGN")
                        .withFirstName("fname")
                        .withLastName("lname")
                        .withIp("127.0.0.1")
                        .withMeta(Lists.<Map<String,Object>>newArrayList())
                        .withNarration("Narration")
                        .build();
```

## Using Rave Dialog with RaveDialog.OnRaveResponseCallback
You can also use the other Rave Dialog Constructor that accepts a third Parameter : RaveDialog.OnRaveResponseCallback if you need access to the final success response.
```
public class MainActivity extends AppCompatActivity implements RaveDialog.OnRaveResponseCallback {

    private Button raveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        raveButton = (Button)findViewById(R.id.rave_btn);

        raveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create rave data object
                // All Constructor parameters must be provided.
                RaveData raveData = new RaveData.Builder(
                        "Shawarma and Coke", // item name
                        "Shawarma and Coke for kenny", // item description
                        1400.00, // item price or 0.0 if you want the user to input the amount
                        "FLWPUBK-XXXXX", // public key
                        "FLWSECK-XXXXX", // secret key
                        "test@gmail.com", // buyer's email address
                        RaveUtil.getTxRef(), // tx-ref which must beunique for a given transaction
                        RaveAuthModel, //The AuthModel to use for the Current trasaction.
                        )
                        .build();

                // initialize RaveDialog using RaveData object created above and call show() to display dialog.
                RaveDialog rave = new RaveDialog(MainActivity.this, raveData);
                rave.show();
            }
        });
    }
}

@Override
    public void onResponse(RaveResponse response) {
        Log.d(TAG, response.toString());
    }
    
```




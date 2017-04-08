# Rave

Rave is an android plugin for interacting with the Flutterwave payment api. It's easy to use and integrate into your existing project. Also, it takes away the extra UI design work, Rave has a nice custom dialog box (both portrait  landscape) for collecting transaction details from users. 

## Getting Started

Before you use Rave, you must have signed up for a staging or testing account on Ravepay via this link:

[Rave - For Testing ](http://rave.frontendpwc.com/)

[Rave - For Production](https://ravepay.co/)

Before u can use the SDk, you need a secrete key and a private key which you will get the moment you create either a staging or production account.
The moment you create an account, you also need to create a rave button so that you can get these keys stated earlier. Also
not the AuthModel you chose at the point of creating the button.


### Prerequisites

There is no any special thing to do before installing the plugin, all you need do is to create a testing account 
or a production account depending on your need as stated above in Getting Started. 

Also you must ensure that your app has internet permissions by 
making sure the uses-permission line below is present in the AndroidManifest.xml.

```

<uses-permission android:name="android.permission.INTERNET" />
```

### Installing

#### Android Studio (Using Gradle Build)
Rave is currently available in Sonatype as SNAPSHOT, not yet release, so you can follow the following steps to grab the 
snapshot for the time being.

- Add the following to you module level build.gradle

```

epositories {
    mavenCentral()
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots"
    }

}

```

- Then include the rave dependency in the dependency section as shown below

```
dependencies {

    ....
    
    compile 'me.jerryhanks.rave:rave-dialog:0.0.2-SNAPSHOT'
    
    ....

```

#### Eclips 

To use this library with Eclipse, you need to:

1. Clone the repository.
2. Import the rave project into your Eclipse project
3. In your project settings, add the Rave project under the Libraries section of the Android category.

Build your project and ensure that all things are working fine, an that is it, you now have Rave
installed

- Initialise Rave before every other thing.
Rave requires you to specify and Application context and an Environment which defaults to Testing if not
set. So do the following in your Application class or MainActivity OnCreate() method

```
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Rave.initEnvironment(this,Rave.ENV_TESTING);
    }
}
    
```

Note: The static method Rave.initEnvironment can take 
* Rave.ENV_PRODUCTION
* Rave.ENV_TESTING

depending on your needs.

## Usage

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

#### Using Rave Dialog with RaveDialog.OnRaveResponseCallback
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


## Running the tests

Explain how to run the automated tests for this system

## Demo App
You can run the demo app by cloning the Repo and importing into Android Studio. 

You can view the screenshots for the demo app [Here](/screenshots) 

You can Use the Following Test cards for all your testing:

#### Test Cards

##### Successful charge:
Card No: 5438898014560229

CVV: 789

Expiry Month: 09

Expiry Year: 19

Pin: 9890


##### Fraudulent
Card No: 5590131743294314

CVV: 887

Expiry Month: 11

Expiry Year: 20

Pin: 8877

##### Insufficient Funds
Card No: 5258585922666506

CVV: 883

Expiry Month: 09

Expiry Year: 17

Pin: 9891

##### Declined
Card No: 5143010522339965

CVV: 276

Expiry Month: 08

Expiry Year: 19

Pin: 4322


## Versioning

We use [Semantic Versioning (SemVer)](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/po10cio/Rave/tags). 

## Authors

* **Jerry Hanks** - *Initial work* - [Andela KShittu](https://github.com/andela-Kshittu/Rave)

See also the list of [contributors](https://github.com/po10cio/Rave/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* [Andela KShittu](https://github.com/andela-Kshittu/Rave)
* [Temi](https://github.com/temiadesina)


## Change Log
Please see [CHANGELOG](CHNAGELOG.md) for more information what has changed with most recent version.


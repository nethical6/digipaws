<p align="center">
  <img src="fastlane/metadata/android/en-US/images/icon.png" style="width: 30%;" />
</p>

# DigiPaws
[![GitHub contributors](https://img.shields.io/github/contributors/nethical6/digipaws)](https://github.com/nethical6/digipaws/graphs/contributors)
[![Discord](https://img.shields.io/discord/1249825403665780786)](https://discord.com/invite/Vs9mwUtuCN)
[![Total downloads](https://img.shields.io/github/downloads/nethical6/digipaws/total)](https://github.com/nethical6/digipaws/releases)
[![Repository Size](https://img.shields.io/github/repo-size/nethical6/digipaws)](https://github.com/nethical6/digipaws)


DigiPaws is an open-source Android productivity utility designed to help users reduce screen addiction by regulating app usage through a gamified experience. The application offers three distinct modes—Easy, Adventure, and Hard—each with unique features and challenges to promote healthy screen time habits.

> [!IMPORTANT]  
> This project is in its extreme initial stages for now

> [!CAUTION]
> This app may be discontinued very soon due to lack of funds (I am an indie developer who codes on phone). [Donate](https://www.patreon.com/nethical)


## Features

- **Three Unique Modes**: Tailor your screen usage experience with Easy, Adventure, and Hard modes.
- **Gamified Challenges**: Earn Aura coins through quests like walking, and use them to unlock app usage.
- **Open Source**: Fully transparent and free to use, with the source code available for community contributions.
- **Productivity Enhancement**: Helps build healthier digital habits and reduce screen addiction.
- **Versatile Blockers**: Take control of your digital environment by blocking apps, keywords, and unwanted in-app screens (e.g., YouTube shorts, comments).
- **Open Api**: Turn your existing application into a digipaws quest!! [Learn More](https://nethical6.github.io/digipaws/partners/)


## Modes

### Easy Mode

In Easy Mode, DigiPaws provides gentle reminders to help you manage your screen time. When you attempt to access a blocked app, a warning screen appears. If you choose to proceed, a cooldown period starts. After the cooldown, another warning is displayed to remind you of your usage.

### Adventure Mode

Adventure Mode introduces a gamified experience to control screen time. To access a blocked app, you need Aura coins. These can be earned by completing quests such as a quick 1 km walk. Each Aura coin grants 20 minutes of usage time for any blocked app, encouraging physical activity and mindful screen use.

#### Quests
1. **Touch Grass** - Displace yourself by 1 km.
2. **40x Squats** - Perform 40 reps of squats in any sets. Verified using AI.
3. **40x Pushups** - Perform 40 reps of pushups in any sets. Verified using AI.
4. **Focus Quest** - Apply the 20/60 study rule: Digipaws blocks all unnecessary applications except basic apps like calling and SMS.
5. **Open API** - See the list of [applications using our quest API.](https://github.com/nethical6/digipause/blob/main/HowToUseApi.md)

> [!IMPORTANT]  
> Remember that only focus quest is available on lite version (same as the fdroid version) to reduce app size. Download the full version to enjoy all inbuilt quests. [Download](https://github.com/nethical6/digipaws/releases)

### Hard Mode

Hard Mode offers the most stringent control over app usage. In this mode, when you try to open a blocked app, the back button is automatically pressed without any warning or cooldown, preventing access entirely.

## Variants
There are two build variants with different features:

- `fullMode:` Contains additional inbuilt quests like touch grass, and workout. Requires google play services.
- `liteMode:` Contains a bare version without additional quests except focus quest. Relatively smaller in size and doesn't require google play services.

Note that the quest api works with both variants

## Configuring

1. Launch DigiPaws on your Android device.
2. Provide all necessary permissions like Accessibility service, Notification, Draw over other apps etc
3. On Android 13+ devices, you need to additionally allow restricted settings before enabling the accessibility permission. Watch a tutorial [here](https://youtu.be/91B72lEpcqc?si=PCKKUSwM1aLdELqJ)
4. Choose your preferred mode: Easy, Adventure, or Hard.
5. Configure the apps and views you want to block and set your preferences.
6. Start using your device with DigiPaws managing your screen time.


> [!TIP]  
> This app relies exclusively on accessibility services to function. Because it requires sensitive permissions, please avoid downloading it from untrusted sources.

## ToDo
- [x] Block reels
- [x] Block comments
- [x] Block explicit context
- [x] App blockers
- [x] Focus Quest
- [x] Anti-Uninstall
- [ ] Geoblocker (basically block things when a certain area is entered like workplace)
- [x] Customisable warning screen
- [ ] Block cusomtom user defined keywords
- [ ] Modular and downloadable view blockers
- [x] Api for other developers to transform their existing apps into digipaw quests!
- [ ] expand the app to ios and desktops.

## Thanks 
- [**Osmdroid**](https://github.com/osmdroid/osmdroid) : Mapview used in Quest "TouchGrass"
- [**Digital Wellbeing Experiments Toolkit**](https://github.com/googlecreativelab/digital-wellbeing-experiments-toolkit) : some ideas ig, basically the geoblocker one.
- [**Undraw**](https://undraw.co) : illustrations on intro page
- Special thanks to all the premium apps on the Play Store that inspired me to create a free and open-source alternative. imagine making money out of miserable people.
- [**Ml Kit QuickStart**](https://github.com/googlesamples/mlkit/tree/master) : ML models and multiple classes were used from this repository.


## Installation

To install DigiPaws:

1. Clone the repository:
    ```sh
    git clone https://github.com/nethical6/digi-paws.git
    ```
2. Open the project in Android Studio.
3. Build and run the app on your Android device.

Alternatively, you can download the latest release from the [Releases](https://github.com/nethical6/digi-paws/releases) page and install the APK on your device.

## Contributing

We welcome contributions from the community! If you'd like to contribute, please follow these steps:

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
    ```sh
    git checkout -b feature/your-feature-name
    ```
3. Commit your changes.
    ```sh
    git commit -m "Add some feature"
    ```
4. Push to the branch.
    ```sh
    git push origin feature/your-feature-name
    ```
5. Create a new Pull Request.

Please ensure your code adheres to our coding standards and includes relevant tests.

## Contributors
- [Henry Delallal](https://github.com/HenriDellal): assisted in setting up fdroid version
- [Remaker17](https://github.com/remaker17): setup workflow files

### HELP REQUIRED!!
1. Implementing material ui inside the warning overlay and also making it customisable.
2. Expanding the list of [explict keywords](https://github.com/nethical6/digipause/blob/da5723915412277655aea4dd448cdc64a21fcc83/app/src/main/java/nethical/digipaws/data/BlockerData.java#L28)
3. Expanding the list of [Blocked view items](https://github.com/nethical6/digipause/blob/main/app/src/main/java/nethical/digipaws/data/BlockerData.java#L4) so that more apps can be supported. You can scrape the view ids of a screen with the help of [Developer Assistant](https://play.google.com/store/apps/details?id=com.appsisle.developerassistant). Remeber that these view ids must be unique and only present on the screen to block!!
4. Adding more info about other packages [here](https://github.com/nethical6/digipause/blob/main/app/src/main/java/nethical/digipaws/data/BlockerData.java)


## License

DigiPaws is licensed under the [GNU License](LICENSE). You are free to use, modify, and distribute this software in accordance with the license. 

## Contact

For questions, suggestions, or feedback, please open an issue on the [GitHub repository](https://github.com/nethical6/digipaws/issues) or contact me at:
1. Discord: @nethical
2. Telegram: @nethicalps

---

Thank you for using DigiPaws! Together, we can create healthier digital habits.

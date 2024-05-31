# DigiPaws: Reclaim Your Life

DigiPaws is an open-source Android productivity utility designed to help users reduce screen addiction by regulating app usage through a gamified experience. The application offers three distinct modes—Easy, Adventure, and Hard—each with unique features and challenges to promote healthy screen time habits.


## Features

- **Three Unique Modes**: Tailor your screen usage experience with Easy, Adventure, and Hard modes.
- **Gamified Challenges**: Earn DigiCoins through quests like walking, and use them to unlock app usage.
- **Open Source**: Fully transparent and free to use, with the source code available for community contributions.
- **Productivity Enhancement**: Helps build healthier digital habits and reduce screen addiction.
- **Versatile Blockers**: Take control of your digital environment by blocking apps, keywords, and unwanted in-app screens (e.g., YouTube shorts, comments).


## Modes

### Easy Mode

In Easy Mode, DigiPaws provides gentle reminders to help you manage your screen time. When you attempt to access a blocked app, a warning screen appears. If you choose to proceed, a cooldown period starts. After the cooldown, another warning is displayed to remind you of your usage.

### Adventure Mode

Adventure Mode introduces a gamified experience to control screen time. To access a blocked app, you need DigiCoins. These can be earned by completing quests such as a quick 1 km walk. Each DigiCoin grants 20 minutes of usage time for any blocked app, encouraging physical activity and mindful screen use.

#### Quests
1. **Touch Grass** - Displace yourself by 1km
2. more quests comming soon. Drop ideas on my dm.

### Hard Mode

Hard Mode offers the most stringent control over app usage. In this mode, when you try to open a blocked app, the back button is automatically pressed without any warning or cooldown, preventing access entirely.

## Usage

1. Launch DigiPaws on your Android device.
2. Provide all necessary permissions like Accessibility service, Notification, Draw over other apps etc
3. Choose your preferred mode: Easy, Adventure, or Hard.
4. Configure the apps and views you want to block and set your preferences.
5. Start using your device with DigiPaws managing your screen time.

> [!CAUTION]  
> This app relies exclusively on accessibility services to function. Because it requires sensitive permissions, please avoid downloading it from untrusted sources.

## ToDo
- [x] Block reels
- [x] Block comments
- [x] Block explicit context
- [x] App blockers
- [ ] Survival Mode (basically blocking everything except calling and sms)
- [ ] Geoblocker (basically block things when a certain area is entered like workplace)
- [ ] Customisable warning screen
- [ ] Block cusomtom user defined keywords
- [ ] Modular and downloadable view blockers
- [ ] Api for other developers to transform their existing apps into digipaw quests! (help needed)
- [ ] expand the app to ios and desktops.
## Thanks 
- [**Osmdroid**](https://github.com/osmdroid/osmdroid) : Mapview used in Quest "TouchGrass"
- [**Digital Wellbeing Experiments Toolkit**](https://github.com/googlecreativelab/digital-wellbeing-experiments-toolkit) : some ideas ig, basically the geoblocker one.
- [**Undraw**](https://undraw.co) : illustrations on intro page
- Special thanks to all the premium apps on the Play Store that inspired me to create a free and open-source alternative. imagine making money out of miserable people.



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
    
### HELP REQUIRED!!
1. Implementing material ui inside the warning overlay and also making it customisable.
2. Implementing a custom api/service through which other apps like a pomodoro can also act like quests. A basic api to implement/decrement coins has been created but this is very insecure as fake coins can be created. A way is required to prevent fake quests.
3. Expanding the list of explict keywords
4. Expanding the list of Blocked views so that more apps can be supported. You can scrape the view ids of a screen with the help of [Developer Assistant](https://play.google.com/store/apps/details?id=com.appsisle.developerassistant). Remeber that these view ids must be unique and only present on the screen to block!!


## License

DigiPaws is licensed under the [GNU License](LICENSE). You are free to use, modify, and distribute this software in accordance with the license. 
> [!IMPORTANT]  
> Uploading any copies/versions/forks of this program to any application sharing platform like playstore without permission is strictly prohibited.

## Contact

For questions, suggestions, or feedback, please open an issue on the [GitHub repository](https://github.com/nethical6/digipaws/issues) or contact me at:
1. Discord: @nethical
2. Telegram: @nethicalps

---

Thank you for using DigiPaws! Together, we can create healthier digital habits.

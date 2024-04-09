# NewGame Template
An empty game project for creating new games using the LintfordLib engine

# GameInfo

You can customize the window size, name and title as well as the size of the game canvas and various resolutions from within the `net.lintfordpickle.newgame.NewGameInfo` class. This class implements the `GameInfo` interface, which is used when creating the window.

# Adding a splash screen

You can add a custom splash screen to the start of the game by adding the following code to the `finializeAppSetup()` in the `NewBaseGame()` class:

``` Java
final var lSplashScreen = new TimedIntroScreen(mScreenManager, "res/textures/textureSplashScreen.png", 1000, 3000);
lSplashScreen.stretchBackgroundToFit(true);

lSplashScreen.setTimerFinishedCallback(new IMenuAction() {
    @Override
    public void TimerFinished(Screen pScreen) {
        mScreenManager.addScreen(new MainMenuBackground(mScreenManager));
        mScreenManager.addScreen(new CreditsScreen(mScreenManager));
        mScreenManager.addScreen(new MainMenu(mScreenManager));
    }
});
```

n.b. If you are using the default GameResourceLoader class, then there will already be a period at the start of the game where another buffer is rendered to the window as the resources are loaded in the background. Generally, this should suffice, and the addition of a second splash screen is not needed/wanted.
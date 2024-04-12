set location=%~dp0

aseprite -b --ignore-empty <absolute path to aseprite file> --save-as "%location%spritesheets\<filename>\{layer}\{tag} 00.png"

pause
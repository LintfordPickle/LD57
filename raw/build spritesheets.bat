@REM Create sprite sheets

set location=%~dp0

Call "D:\dev\tools\spritepacker\SpritePacker.exe" "%location%spritesheets\spritesheet_particles.lss"
Call "D:\dev\tools\spritepacker\SpritePacker.exe" "%location%spritesheets\spritesheet_game.lss"
Call "D:\dev\tools\spritepacker\SpritePacker.exe" "%location%spritesheets\spritesheet_mobs.lss"
Call "D:\dev\tools\spritepacker\SpritePacker.exe" "%location%spritesheets\spritesheet_hud.lss"

pause
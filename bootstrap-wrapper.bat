@echo off
setlocal EnableExtensions
set "GRADLE_VERSION=9.6.0"
set "BOOTSTRAP=%TEMP%\lec10-gradle-bootstrap-%RANDOM%"
set "ZIP=%BOOTSTRAP%\gradle.zip"
set "URL=https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip"

if exist gradlew.bat (
  echo Gradle Wrapper already exists.
  exit /b 0
)

mkdir "%BOOTSTRAP%" >nul 2>&1

echo Downloading Gradle %GRADLE_VERSION%...
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest -UseBasicParsing '%URL%' -OutFile '%ZIP%'"
if errorlevel 1 goto :fail

echo Extracting Gradle...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%ZIP%' -DestinationPath '%BOOTSTRAP%' -Force"
if errorlevel 1 goto :fail

echo Generating Gradle Wrapper...
call "%BOOTSTRAP%\gradle-%GRADLE_VERSION%\bin\gradle.bat" wrapper --gradle-version %GRADLE_VERSION% --distribution-type bin
if errorlevel 1 goto :fail

rmdir /s /q "%BOOTSTRAP%" >nul 2>&1

echo.
echo Gradle Wrapper created successfully.
echo Next command: .\gradlew.bat :app:assembleDebug
exit /b 0

:fail
echo.
echo ERROR: Gradle Wrapper bootstrap failed.
echo Check Internet access and Java installation.
exit /b 1

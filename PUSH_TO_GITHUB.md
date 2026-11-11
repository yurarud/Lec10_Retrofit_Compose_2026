# Публікація на GitHub

Після успішної збірки:

```powershell
git init
git add .
git commit -m "Lecture 10: Retrofit REST API Compose example"
git branch -M main
git remote add origin https://github.com/<USER>/<REPOSITORY>.git
git push -u origin main
```

Після `bootstrap-wrapper.bat` додайте до Git також стандартні файли Gradle Wrapper:

```text
gradlew
gradlew.bat
gradle/wrapper/gradle-wrapper.jar
gradle/wrapper/gradle-wrapper.properties
```

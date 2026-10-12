Сборка: установи JDK 8/11/16, открой папку в IntelliJ (Gradle) или выполни
  gradle wrapper --gradle-version 7.3.3 && ./gradlew build
Готовый jar: build/libs/healthindicator-fabric-1.16.5.jar
Нужен Fabric Loader + Fabric API (0.42.0+1.16). ModMenu необязателен; настройки также на клавише H.
Если Gradle не находит modmenu - удали HealthModMenu.java и блок "modmenu" в fabric.mod.json.

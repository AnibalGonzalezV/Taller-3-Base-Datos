## Taller 3 - Bases de Datos

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Execute
javac -d .\bin\ .\src\*.java
java -cp ".\bin;lib/*" App
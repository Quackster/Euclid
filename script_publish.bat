dotnet publish -c Release -r win10-x64 /p:PublishSingleFile=true /p:PublishTrimmed=true
dotnet publish -c Release -r linux-x64 /p:PublishSingleFile=true /p:PublishTrimmed=true
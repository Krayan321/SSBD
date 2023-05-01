Remove-item src\main\webapp\static -Recurse -ErrorAction:SilentlyContinue
Remove-item src\main\webapp\*.*

Set-Location front-UI
npm run build
Copy-Item -Path "build\*" -Destination "..\src\main\webapp" -Recurse
Set-Location ..
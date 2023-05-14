$PROJECT_ROOT = "$PSScriptRoot\.."

Remove-item "$PROJECT_ROOT\src\main\webapp\assets" -Recurse -ErrorAction:SilentlyContinue
Remove-item "$PROJECT_ROOT\src\main\webapp\static" -Recurse -ErrorAction:SilentlyContinue
Remove-item "$PROJECT_ROOT\src\main\webapp\*.*"

Set-Location "$PROJECT_ROOT\front-UI"
npm run build
Copy-Item -Path "build\*" -Destination "..\src\main\webapp" -Recurse
Set-Location "$PROJECT_ROOT"
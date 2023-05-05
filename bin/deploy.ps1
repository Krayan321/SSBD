$PROJECT_ROOT = "$PSScriptRoot\.."

Set-Location "$PROJECT_ROOT"

Invoke-Expression -Command ".\bin\build_front.ps1"
Invoke-Expression -Command ".\bin\build_backend.ps1"

$WAR = Get-ChildItem -Path "$PROJECT_ROOT\target" *.war | Select-Object -First 1

#asadmin $PAYARA_OPTIONS undeploy "ssbd01-0.0.1"
#asadmin --host 10.31.201.3 --port 4848 --user admin --passwordfile "$PROJECT_ROOT\config\payara-password" deploy --name "ssbd01" "$PROJECT_ROOT\target\ssbd01-0.0.1.war"

asadmin --host 10.31.201.3 --port 4848 --user admin --passwordfile "$PROJECT_ROOT\config\payara-password" redeploy --name "ssbd01" "$PROJECT_ROOT\target\$WAR"

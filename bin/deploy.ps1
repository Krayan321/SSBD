$PROJECT_ROOT = "$PSScriptRoot\.."

Set-Location $PROJECT_ROOT

Invoke-Expression -Command $PROJECT_ROOT\bin\build_front.ps1
Invoke-Expression -Command $PROJECT_ROOT\bin\build_backend.ps1

#asadmin $PAYARA_OPTIONS undeploy "ssbd01-0.0.1"
asadmin --host 10.31.201.3 --port 4848 --user admin --passwordfile $PROJECT_ROOT\config\payara-password redeploy --name "ssbd01-0.0.1" "$PROJECT_ROOT\target\ssbd01-0.0.1.war"

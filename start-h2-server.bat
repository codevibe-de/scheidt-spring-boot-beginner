@echo off
REM Starts a standalone H2 TCP server using the H2 jar from the project's dependencies.
REM Data is stored in .\h2-data\ (persists between restarts).

setlocal
cd /d "%~dp0"

echo Locating H2 jar from project dependencies...
set "CP_FILE=%TEMP%\h2-classpath-%RANDOM%.txt"
REM Only include the h2 artifact: keeps the classpath short (set /p truncates at 1023 chars)
call mvnw.cmd -q dependency:build-classpath -DincludeArtifactIds=h2 -Dmdep.outputFile="%CP_FILE%" >nul 2>&1

if not exist "%CP_FILE%" goto :no_classpath
set "H2_CP="
set /p H2_CP=<"%CP_FILE%"
del "%CP_FILE%" >nul 2>&1
if not defined H2_CP goto :no_classpath

set "DATA_DIR=%CD%\h2-data"
if not exist "%DATA_DIR%" mkdir "%DATA_DIR%"

echo Starting H2 TCP server...
echo   H2 jar: %H2_CP%
echo   TCP:  jdbc:h2:tcp://localhost:9092/./pizzadb
echo   Web console: http://localhost:8082
echo   Data directory: %DATA_DIR%
echo.
echo Press Ctrl+C to stop.
echo.

java -cp "%H2_CP%" org.h2.tools.Server -tcp -tcpPort 9092 -tcpAllowOthers -web -webPort 8082 -baseDir "%DATA_DIR%" -ifNotExists

endlocal
exit /b 0

:no_classpath
if exist "%CP_FILE%" del "%CP_FILE%" >nul 2>&1
echo Could not locate the H2 jar.
echo Make sure com.h2database:h2 is a dependency in pom.xml, then run
echo 'mvnw.cmd dependency:resolve' to populate the local Maven cache.
endlocal
exit /b 1

$ErrorActionPreference = "Stop"

Write-Host "Checking Lecture 10 project..." -ForegroundColor Cyan

$path = (Get-Location).Path
if ($path -match '[^\x00-\x7F]') {
    Write-Host "WARNING: project path contains non-ASCII characters:" -ForegroundColor Yellow
    Write-Host $path -ForegroundColor Yellow
    Write-Host "Recommended: E:\Android\Lec10_Retrofit_Compose_2026" -ForegroundColor Yellow
} else {
    Write-Host "OK: ASCII-only path" -ForegroundColor Green
}

$required = @(
    "settings.gradle.kts",
    "build.gradle.kts",
    "app\build.gradle.kts",
    "app\src\main\AndroidManifest.xml",
    "app\src\main\java\com\example\lec10network\data\remote\PostApi.kt",
    "app\src\main\java\com\example\lec10network\data\repository\NetworkPostRepository.kt",
    "app\src\main\java\com\example\lec10network\ui\NetworkDemoViewModel.kt"
)

foreach ($file in $required) {
    if (-not (Test-Path $file)) {
        throw "Missing required file: $file"
    }
}

Write-Host "OK: required project files exist" -ForegroundColor Green

if (Test-Path ".git") {
    Write-Host "INFO: .git exists - this directory is already a Git repository." -ForegroundColor Yellow
} else {
    Write-Host "OK: clean directory without inherited .git" -ForegroundColor Green
}

if (Test-Path ".\gradlew.bat") {
    Write-Host "OK: Gradle Wrapper exists" -ForegroundColor Green
} else {
    Write-Host "Gradle Wrapper not generated yet. Run .\bootstrap-wrapper.bat" -ForegroundColor Yellow
}

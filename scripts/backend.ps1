param(
  [ValidateSet('run', 'test', 'verify')]
  [string]$Task = 'run'
)

$ErrorActionPreference = 'Stop'
$repositoryRoot = Split-Path -Parent $PSScriptRoot
$envFile = Join-Path $repositoryRoot '.env'
$previousValues = @{}
$exitCode = 1

try {
  if (Test-Path -LiteralPath $envFile) {
    foreach ($line in Get-Content -LiteralPath $envFile) {
      if ([string]::IsNullOrWhiteSpace($line) -or $line.TrimStart().StartsWith('#')) { continue }
      if ($line -notmatch '^([A-Z][A-Z0-9_]*)=(.*)$') {
        throw 'Invalid .env line. Use literal KEY=value lines without export, interpolation, or multiline values.'
      }
      $key = $Matches[1]
      $value = $Matches[2]
      if ($key -notmatch '^(SPRING_PROFILES_ACTIVE|SERVER_PORT|FRONTEND_ORIGIN|SPRING_DATASOURCE_URL|SPRING_DATASOURCE_USERNAME|SPRING_DATASOURCE_PASSWORD)$') { continue }
      # Process settings (for CI/IDE overrides) take precedence. Never evaluate file contents.
      if ($null -eq [Environment]::GetEnvironmentVariable($key, 'Process')) {
        if (-not $previousValues.ContainsKey($key)) { $previousValues[$key] = $null }
        [Environment]::SetEnvironmentVariable($key, $value, 'Process')
      }
    }
  }
  if ($Task -ne 'test') {
    foreach ($key in @('SPRING_DATASOURCE_URL', 'SPRING_DATASOURCE_USERNAME', 'SPRING_DATASOURCE_PASSWORD', 'FRONTEND_ORIGIN')) {
      if ([string]::IsNullOrWhiteSpace([Environment]::GetEnvironmentVariable($key, 'Process'))) {
        throw "Missing $key. Copy .env.example to .env and configure local values, or set process environment variables."
      }
    }
  }
  Push-Location (Join-Path $repositoryRoot 'backend')
  try {
    $goal = if ($Task -eq 'run') { 'spring-boot:run' } else { $Task }
    & '.\mvnw.cmd' $goal
    $exitCode = $LASTEXITCODE
  } finally { Pop-Location }
} finally {
  foreach ($key in $previousValues.Keys) {
    [Environment]::SetEnvironmentVariable($key, $previousValues[$key], 'Process')
  }
}
exit $exitCode

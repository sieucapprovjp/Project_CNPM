#!/usr/bin/env bash
set -euo pipefail

task="${1:-run}"
case "$task" in run|test|verify) ;; *) echo 'Usage: bash scripts/backend.sh [run|test|verify]' >&2; exit 2 ;; esac
repository_root="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"

if [[ -f "$repository_root/.env" ]]; then
  while IFS= read -r line || [[ -n "$line" ]]; do
    line="${line%$'\r'}"
    [[ "$line" =~ ^[[:space:]]*$ || "$line" =~ ^[[:space:]]*# ]] && continue
    if [[ ! "$line" =~ ^([A-Z][A-Z0-9_]*)=(.*)$ ]]; then
      echo 'Invalid .env line. Use literal KEY=value lines without export, interpolation, or multiline values.' >&2
      exit 1
    fi
    key="${BASH_REMATCH[1]}"
    value="${BASH_REMATCH[2]}"
    case "$key" in
      SPRING_PROFILES_ACTIVE|SERVER_PORT|FRONTEND_ORIGIN|SPRING_DATASOURCE_URL|SPRING_DATASOURCE_USERNAME|SPRING_DATASOURCE_PASSWORD)
        # Preserve explicit process settings and never evaluate file contents.
        if ! printenv "$key" >/dev/null; then export "$key=$value"; fi
        ;;
    esac
  done < "$repository_root/.env"
fi

if [[ "$task" != test ]]; then
  for key in SPRING_DATASOURCE_URL SPRING_DATASOURCE_USERNAME SPRING_DATASOURCE_PASSWORD FRONTEND_ORIGIN; do
    if [[ -z "${!key:-}" ]]; then
      echo "Missing $key. Configure .env or process environment variables." >&2
      exit 1
    fi
  done
fi

cd "$repository_root/backend"
[[ "$task" == run ]] && task=spring-boot:run
exec bash ./mvnw "$task"

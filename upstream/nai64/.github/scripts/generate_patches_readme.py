"""Regenerate the patches table in README.md from patches-list.json."""

from __future__ import annotations

import json
import re
import sys


def main() -> None:
    if len(sys.argv) < 4:
        print(f"Usage: {sys.argv[0]} <repo> <branch> <patches-json> [readme]")
        sys.exit(1)

    repo = sys.argv[1]
    branch = sys.argv[2]
    patches_json_path = sys.argv[3]
    readme_path = sys.argv[4] if len(sys.argv) > 4 else "README.md"

    with open(patches_json_path, encoding="utf-8") as f:
        data = json.load(f)

    patches = data.get("patches", [])
    if not patches:
        print("  [i] No patches found, skipping README regeneration.")
        return

    rows = []
    for p in patches:
        name = p["name"]
        desc = p.get("description", "")
        rows.append(f"| **{name}** | {desc} |")

    table = "\n".join(rows)

    with open(readme_path, encoding="utf-8") as f:
        readme = f.read()

    # Replace everything between "## Patches" and the next heading (or EOF)
    start = readme.find("## Patches")
    if start == -1:
        print("  [i] No '## Patches' section found, appending.")
        readme += f"\n\n## Patches\n\n| Patch | Description |\n|-------|-------------|\n{table}\n"
    else:
        # Find the next heading after ## Patches
        rest = readme[start + 10:]
        end = re.search(r"\n## ", rest)
        if end:
            end_pos = start + 10 + end.start()
        else:
            end_pos = len(readme)

        new_section = (
            "## Patches\n\n"
            "| Patch | Description |\n"
            "|-------|-------------|\n"
            f"{table}\n"
        )
        readme = readme[:start] + new_section + readme[end_pos:]

    with open(readme_path, "w", encoding="utf-8", newline="") as f:
        f.write(readme)

    print(f"  Updated README.md with {len(patches)} patches.")


if __name__ == "__main__":
    main()

const version = process.argv[2];

if (!version) {
  console.error("Usage: node scripts/changelog-for-release.js <version>");
  process.exit(1);
}

console.log(`## ${version}`);

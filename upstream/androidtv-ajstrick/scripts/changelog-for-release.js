#!/usr/bin/env node

const fs = require('fs');
const path = require('path');

const version = process.argv[2];
if (!version) {
  console.error('Usage: changelog-for-release.js <version>');
  process.exit(1);
}

const changelogPath = path.join(__dirname, '..', 'CHANGELOG.md');

if (!fs.existsSync(changelogPath)) {
  console.log(`Release v${version}`);
  process.exit(0);
}

const changelog = fs.readFileSync(changelogPath, 'utf8');
const lines = changelog.split('\n');

let capture = false;
const notes = [];

for (const line of lines) {
  if (line.startsWith(`## [${version}]`) || line.startsWith(`## ${version}`)) {
    capture = true;
    continue;
  }
  if (capture && line.startsWith('## ')) {
    break;
  }
  if (capture) {
    notes.push(line);
  }
}

const output = notes.join('\n').trim();
console.log(output || `Release v${version}`);

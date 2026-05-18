#!/usr/bin/env bash
set -euo pipefail

BASE_DIR="src/test/resources/payloads"

mkdir -p "${BASE_DIR}/base"
mkdir -p "${BASE_DIR}/fragments"
mkdir -p "${BASE_DIR}/generated"

cat > "${BASE_DIR}/base/minimal-valid-template.xml" <<'EOF'
<DeclarationValidationRequest>
    <referenceDate>2026-05-11T00:00:00</referenceDate>
    <declaration>
        <versionNumber>1</versionNumber>
        <creationDate>2026-05-11T00:00:00</creationDate>
        <reference>LOCAL-TEST-DECLARATION</reference>
        <acceptanceDate>2026-05-11T00:00:00</acceptanceDate>
        <type>A</type>
        <procedureCategory>H1</procedureCategory>
    </declaration>
    <externalPartyID>LOCAL_TEST</externalPartyID>
    <topic>DECLARATION_VALIDATION</topic>
</DeclarationValidationRequest>
EOF

cat > "${BASE_DIR}/fragments/goods-item-core.xml" <<'EOF'
<goodsItem>
    <sequenceNumber>1</sequenceNumber>
</goodsItem>
EOF

cat > "${BASE_DIR}/fragments/previous-document-core.xml" <<'EOF'
<previousDocument>
    <sequenceNumber>1</sequenceNumber>
    <type>N355</type>
    <reference>LOCAL-PREV-DOC</reference>
</previousDocument>
EOF

cat > "${BASE_DIR}/fragments/additional-document-core.xml" <<'EOF'
<additionalDocument>
    <sequenceNumber>1</sequenceNumber>
    <type>C505</type>
    <reference>LOCAL-ADD-DOC</reference>
</additionalDocument>
EOF

cat > "${BASE_DIR}/fragments/additional-information-core.xml" <<'EOF'
<additionalInformation>
    <sequenceNumber>1</sequenceNumber>
    <code>LOCAL</code>
    <text>Local generated test information</text>
</additionalInformation>
EOF

echo "Created payload template folders and XML fragments under:"
echo "  ${BASE_DIR}"

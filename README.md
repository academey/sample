1.KTLINT - PRE COMMIT 적용하기 위한 조건 

- GIT HOOK 경로 변경 (명령어 : git config core.hooksPath .githooks)
- 경로 변경 확인시 .githooks로 표기 (명령어: git config --get core.hooksPath)

2. 실행 방법
./gradlew composeUp (DB 띄울 때 한번만)
./gradlew bootRun




lsof -n -i TCP:8080
kill -9 프로세스id

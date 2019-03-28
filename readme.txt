1. 이클립스에서 Run Configuration 클릭

2. Gradle에 New Configuration 만들고 Gradle Tasks: 란에 build추가
ex)
Gradle Tasks:
build

3. Working Directory: 는 이 프로젝트로 지정
ex)
${workspace_loc:/SpringBatchStudy}

4. Java Home: 지정
C:\Program Files\Java\Jdk 폴더

5. Common - Standard Input and Output (이 프로젝트로 지정)
Output File: ${workspace_loc:/SpringBatchStudy}

5. 생성한 Run Configuration으로 실행(Run)

6. Gradle로 실행하면 프로젝트 폴더 내 /build에 작업 output이 생성된다.



7. bat 파일 스크립트 내용
java -jar {Gradle로 빌드한 jar파일 경로}
pause>nul (배치 프로그램 처리 중 종료되지 않도록 커맨드라인 띄운 상태 유지)

ex)
java -jar D:\SpringBatchStudy.jar --spring.profiles.active=postgres --job.name=myJob
pause>nul



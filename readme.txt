1. ��Ŭ�������� Run Configuration Ŭ��

2. Gradle�� New Configuration ����� Gradle Tasks: ���� build�߰�
ex)
Gradle Tasks:
build

3. Working Directory: �� �� ������Ʈ�� ����
ex)
${workspace_loc:/SpringBatchStudy}

4. Java Home: ����
C:\Program Files\Java\Jdk ����

5. Common - Standard Input and Output (�� ������Ʈ�� ����)
Output File: ${workspace_loc:/SpringBatchStudy}

5. ������ Run Configuration���� ����(Run)

6. Gradle�� �����ϸ� ������Ʈ ���� �� /build�� �۾� output�� �����ȴ�.



7. bat ���� ��ũ��Ʈ ����
java -jar {Gradle�� ������ jar���� ���}
pause>nul (��ġ ���α׷� ó�� �� ������� �ʵ��� Ŀ�ǵ���� ��� ���� ����)

ex)
java -jar D:\SpringBatchStudy.jar --spring.profiles.active=postgres --job.name=myJob
pause>nul



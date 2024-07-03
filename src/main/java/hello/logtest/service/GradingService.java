package hello.logtest.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.springframework.stereotype.Service;

@Service
public class GradingService {

    public boolean compileAndRun(String filePath) {
        Process compileProcess = null;
        Process runProcess = null;
        try {
            // Java 파일 컴파일
            ProcessBuilder compileBuilder = new ProcessBuilder("javac", filePath);
            compileProcess = compileBuilder.start();

            // 컴파일 에러 출력
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(compileProcess.getErrorStream()));
            String errorLine;
            boolean compileErrors = false;
            while ((errorLine = errorReader.readLine()) != null) {
                System.err.println(errorLine);
                compileErrors = true;
            }

            int compileResult = compileProcess.waitFor(); // 컴파일 완료 대기
            if (compileResult != 0 || compileErrors) {
                System.out.println("Compilation failed.");
                return false;
            }

            // 컴파일된 클래스 실행
            String className = filePath.substring(filePath.lastIndexOf("/") + 1).replace(".java", "");
            ProcessBuilder runBuilder = new ProcessBuilder("java", className);
            runProcess = runBuilder.start();

            // 실행 결과 출력
            BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int runResult = runProcess.waitFor(); // 프로세스 종료 대기
            return runResult == 0;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (compileProcess != null) {
                compileProcess.destroy();
            }
            if (runProcess != null) {
                runProcess.destroy();
            }
        }
    }
}
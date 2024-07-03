package hello.logtest.controller;

import hello.logtest.dto.SolutionRequest;
import hello.logtest.service.GradingService;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final GradingService gradingService;

    @PostMapping("/submit")
    public ResponseEntity<String> receiveSolution(@RequestBody SolutionRequest request) {
        String answerCode = request.getAnswerCode();

        // 현재 프로젝트 루트 디렉토리에 파일을 저장
        String filename = UUID.randomUUID().toString() + "_Solution.java";
        Path filePath = Paths.get("./" + filename);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(answerCode);
        } catch (IOException e) {
            logger.error("쓰기 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("쓰기 실패" + e.getMessage());
        }

        // 컴파일 및 실행
        boolean result = gradingService.compileAndRun(filePath.toString());
        if (result) {
            return ResponseEntity.ok("Solution is correct");
        } else {
            return ResponseEntity.ok("Solution is incorrect or failed to compile/run");
        }
    }
}

package hello.logtest;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class DataGenerator {
    public void generateInputFile() throws IOException {
        Random random = new Random();
        try (PrintWriter out = new PrintWriter("input.txt")) {
            for (int i = 0; i < 100; i++) { // 예시로 100개의 데이터 생성
                out.println(random.nextInt(1000)); // 0부터 999까지의 랜덤 숫자
            }
        }
    }
}

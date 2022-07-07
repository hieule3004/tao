package com.template.tao;

import com.template.tao.model.divination.bookofchanges.BookOfChanges;
import com.template.tao.model.divination.bookofchanges.Hexagram;
import com.template.tao.utils.MathUtils;
import io.vavr.collection.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

//@SpringBootApplication
public class TaoApplication {

  public static void main(String[] args) throws Exception {
    String command = "jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -storepass \"WhirlyGig16 \" -keystore ../KeyStore/KeyStore.jks app\\build\\outputs\\apk\\release\\app-release-unsigned.apk \"blacktrace android\"";
//    command = "";
    ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
    Process process = processBuilder.start();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String output = reader.lines().collect(Collectors.joining("\n"));
      System.out.println(output);
    }
//    SpringApplication.run(TaoApplication.class);


//    List.range(0, 8)
//            .forEach(i -> {
//              List.range(0, 8).forEach(j -> System.out.print(BookOfChanges.fromSequenceIndex(8 * i + j + 1) + " "));
//              System.out.println();
//            });

//    long start = System.currentTimeMillis();
//    BookOfChanges.SYMMETRIC_GROUPS.forEach(System.out::println);
//    System.out.printf("time = %d ms%n", System.currentTimeMillis() - start);
//    System.out.println();
//    BookOfChanges.BOOK
//        .grouped(2)
//        .map(hs -> print(hs.get(0)) + " " + print(hs.get(1)))
//        .forEach(System.out::println);
  }

  private static String print(Hexagram h) {
    return String.format("%s %s", h, MathUtils.toBinaryString(h.getValue(), 6));
  }
}

package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final int textLength = 10000;
    public static void main(String[] args) throws InterruptedException {
        String LETTERS = "abc";
        int LETTERS_LENGTH = 100000;
        char A = 'a';
        char B = 'b';
        char C = 'c';


        BlockingQueue<String> aQueue = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> cQueue = new ArrayBlockingQueue<>(100);

        Thread textGeneratorThread = new Thread(() -> {
            for (int i = 0; i < textLength; i++) {
                String generatedText = generateText(LETTERS, LETTERS_LENGTH);
                try {
                    aQueue.put(generatedText);
                    bQueue.put(generatedText);
                    cQueue.put(generatedText);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        textGeneratorThread.start();

        Thread aThread = new Thread(() -> System.out.println("Max A symbol: " + getMaxLength(aQueue, A)));
        aThread.start();
        Thread bThread = new Thread(() -> System.out.println("Max B symbol: " + getMaxLength(bQueue, B)));
        bThread.start();
        Thread cThread = new Thread(() -> System.out.println("Max C symbol: " + getMaxLength(cQueue, C)));
        cThread.start();

        aThread.join();
        bThread.join();
        cThread.join();

    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static int getMaxLength(BlockingQueue<String> queue, char symbol) {
        int count = 0;
        int max = 0;
        try {
            for (int i = 0; i < textLength; i++) {
                String text = queue.take();
                for (char c : text.toCharArray()) {
                    if (c == symbol) {
                        count++;
                    }
                }
                if (count > max) {
                    max = count;
                }
                count = 0;
            }
            return max;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
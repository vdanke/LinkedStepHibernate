package org.step.linked.step;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Runner {

    public static int maxRepeatNumber(Integer... numbers) {
        Map<Integer, Integer> numberMap = new HashMap<>();
        for (Integer number : numbers) {
            if (numberMap.containsKey(number)) {
                numberMap.put(number, numberMap.get(number) + 1);
                continue;
            }
            numberMap.put(number, 1);
        }
        class Node {
            int num; //0
            int count; //0
        }
        Node node = new Node();
        Set<Map.Entry<Integer, Integer>> entries = numberMap.entrySet();
        entries
                .forEach(entry -> {
                    if (entry.getValue() > node.count) {
                        node.num = entry.getKey();
                        node.count = entry.getValue();
                    }
                    if (entry.getValue() == node.count) {
                        if (node.num < entry.getKey()) {
                            node.num = entry.getKey();
                        }
                    }
                });
        return node.num;
    }

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        int i = maxRepeatNumber(12, 12, 12, 3, 3, 3);
        System.out.println(i);
    }
}
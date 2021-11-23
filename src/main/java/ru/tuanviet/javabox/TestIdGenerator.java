package ru.tuanviet.javabox;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TestIdGenerator {
    private static TestIdGenerator generator;
    private final List<UUID> idList;

    private TestIdGenerator() {
        idList = new ArrayList<>();
    }

    public static TestIdGenerator create() {
        if (generator == null) {
            generator = new TestIdGenerator();
        }
        return generator;
    }

    public UUID generateId() {
        UUID testId = UUID.randomUUID();
        idList.add(testId);
        return testId;
    }

    public UUID get(int index) {
        return idList.get(index);
    }

}

//UUID testId = TestIdGenerator.create().generateId();

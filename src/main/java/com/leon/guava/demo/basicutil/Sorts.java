package com.leon.guava.demo.basicutil;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: chenliang
 * @Date: 2020年12月11
 */
public class Sorts {

    public static void main(String[] args) {

    }

    public static void newOrdering() {
        Ordering<String> ordering = new Ordering<String>() {
            @Override
            public int compare(@Nullable String left, @Nullable String right) {
                return Ints.compare(left.length(), right.length());
            }
        };

    }

    public static void orderingFrom() {
        User user1 = new User("leon", 15);
        User user2 = new User("chenliang", 18);
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        //构造排序器
        Ordering<User> ordering = Ordering.from((left, right) -> Ints.compare(left.age, right.age));
        //获取排序结果
        List<User> usersSorted = ordering.immutableSortedCopy(users);
    }

    public static void orderingNatrual() {
        int[] arr = {7, 3, 5, 2, 8};
        Ordering<Integer> ordering = Ordering.natural();
        List<Integer> list = ordering.immutableSortedCopy(Arrays.stream(arr).boxed().collect(Collectors.toList()));
    }

    public static void useToStringOrdering() {
        List<String> list = Lists.newArrayList("leon", "chenliang");
        Ordering ording = Ordering.usingToString();
        List<String> listSorted = ording.sortedCopy(list);
    }

    public static void sortTest() {
        Ordering<User> ordering = Ordering.natural().nullsFirst().onResultOf(user -> {
            return user.age;
        });
    }

    public static void streamSort(){
        User user1 = new User("leon", 15);
        User user2 = new User("chenliang", 18);
        List<User> users = new ArrayList<User>();
        users.add(user1);
        users.add(user2);

        List<User> usersSort = users.stream().sorted().collect(Collectors.toList());
        List<User> userSort1 = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).collect(Collectors.toList());
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private String name;
        private Integer age;
    }
}
